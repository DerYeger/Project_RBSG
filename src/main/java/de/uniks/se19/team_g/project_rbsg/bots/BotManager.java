package de.uniks.se19.team_g.project_rbsg.bots;

import de.uniks.se19.team_g.project_rbsg.ingame.IngameContext;
import de.uniks.se19.team_g.project_rbsg.model.Game;
import de.uniks.se19.team_g.project_rbsg.model.GameProvider;
import de.uniks.se19.team_g.project_rbsg.model.User;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.server.rest.JoinGameManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.LoginManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.user.GetTempUserService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Component
public class BotManager {

    private final GetTempUserService getTempUserService;
    private final LoginManager loginManager;
    private final ObjectProvider<RestTemplate> templateBuilder;
    private final ObjectProvider<JoinGameManager> joinGameManagerProvider;
    private final ObjectProvider<IngameContext> contextFactory;

    public BotManager(
            GetTempUserService getTempUserService,
            LoginManager loginManager,
            @Qualifier("rbsgTemplate") ObjectProvider<RestTemplate> templateBuilder,
            ObjectProvider<JoinGameManager> joinGameManagerProvider,
            @Qualifier("dedicatedContext") ObjectProvider<IngameContext> contextFactory
    ) {
        this.getTempUserService = getTempUserService;
        this.loginManager = loginManager;
        this.templateBuilder = templateBuilder;
        this.joinGameManagerProvider = joinGameManagerProvider;
        this.contextFactory = contextFactory;
    }

    private Context buildBotContext(Game gameData, User user) {

        Context context = new Context();
        context.user = user;
        context.gameData = gameData;
        context.rbsgTemplate = templateBuilder.getObject(new UserProvider().set(context.user));

        return context;
    }

    public void requestBot(Game game) {

        CompletableFuture<Context> userCreationPromise = CompletableFuture.supplyAsync(getTempUserService)
                .thenApplyAsync(loginManager::login)
                .thenApply(user -> buildBotContext(game, user));

        CompletableFuture<IngameContext> joinGamePromise = userCreationPromise.thenApplyAsync(context -> {
            joinGameManagerProvider
                    .getObject(context.rbsgTemplate)
                    .doJoinGame(context.user, context.gameData)
            ;
            return context;
        }).thenApply(context -> contextFactory.getObject(context.user, context.gameData))
                .thenApply(ingameContext -> ingameContext.boot(false));

        CompletableFuture<Context> createArmyPromise = userCreationPromise.thenApplyAsync(
                context -> context,
                CompletableFuture.delayedExecutor(4, TimeUnit.SECONDS)
        );

        joinGamePromise.thenCombine(
                createArmyPromise,
                (ingameContext, context) -> {
                    ingameContext.getGameEventManager().terminate();
                    return ingameContext;
                }
        );
    }

    public static class Context {
        public RestTemplate rbsgTemplate;
        public User user;
        public Game gameData;
    }
}
