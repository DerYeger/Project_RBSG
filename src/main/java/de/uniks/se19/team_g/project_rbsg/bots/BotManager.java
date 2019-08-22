package de.uniks.se19.team_g.project_rbsg.bots;

import de.uniks.se19.team_g.project_rbsg.model.Game;
import de.uniks.se19.team_g.project_rbsg.server.rest.LoginManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.user.GetTempUserService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
public class BotManager {

    private Map<String, Bot> bots = Collections.synchronizedMap(new HashMap<>());

    private final GetTempUserService getTempUserService;
    private final LoginManager loginManager;
    private final ObjectProvider<Bot> botFactory;

    public BotManager(
            ObjectProvider<Bot> botFactory,
            GetTempUserService getTempUserService,
            LoginManager loginManager
    ) {
        this.getTempUserService = getTempUserService;
        this.loginManager = loginManager;
        this.botFactory = botFactory;
    }

    public void requestBot(Game game) {

        CompletableFuture<Bot> botBooting = CompletableFuture.supplyAsync(getTempUserService)
                .thenApplyAsync(loginManager::login)
                .thenApply( user -> botFactory.getObject().start(game, user))
                .thenCompose(Bot::getBootPromise)
        ;

        botBooting
                .thenApplyAsync(bot -> bots.put(bot.getName(), bot))
                .exceptionally(throwable -> {throwable.printStackTrace(); return null;});


    }

    @PreDestroy
    public void shutdown() {
        bots.forEach((s, bot) -> bot.shutdown());
    }
}
