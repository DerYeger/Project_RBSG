package de.uniks.se19.team_g.project_rbsg.bots;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import de.uniks.se19.team_g.project_rbsg.model.Game;
import de.uniks.se19.team_g.project_rbsg.server.rest.LoginManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.user.GetTempUserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.PreDestroy;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
public class BotManager {

    private Map<String, Bot> bots = Collections.synchronizedMap(new HashMap<>());
    private ObservableMap<String, Bot> botObservableList = FXCollections.observableMap(bots);

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

    public CompletableFuture<Bot> requestBot(Game game) {

        CompletableFuture<Bot> botBooting = CompletableFuture.supplyAsync(getTempUserService)
                .thenApplyAsync(loginManager::login)
                .thenApply( user -> {
                    final Bot bot = botFactory.getObject();
                    botObservableList.put(user.getName(), bot);
                    return bot.start(game, user);
                })
                .thenCompose(Bot::getBootPromise)
        ;

        botBooting
                .exceptionally(throwable -> {throwable.printStackTrace(); return null;});

        return botBooting;
    }

    public Collection<Bot> getBots() {
        ObservableList botList = (ObservableList) bots.values();
        return bots.values();
    }

    @PreDestroy
    public void shutdown() {
        bots.forEach((s, bot) -> bot.shutdown());
    }

    public void shutdown(Bot bot){
        bot.shutdown();
    }

    public ObservableMap<String, Bot> getBotObservableMap() {
        return botObservableList;
    }

    @Nullable
    public Bot getAssociatedBot(@Nonnull Player p) {
        return bots.get(p.getName());
    }
}
