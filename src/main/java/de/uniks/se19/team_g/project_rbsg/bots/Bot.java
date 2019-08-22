package de.uniks.se19.team_g.project_rbsg.bots;

import de.uniks.se19.team_g.project_rbsg.ingame.IngameContext;
import de.uniks.se19.team_g.project_rbsg.model.Game;
import de.uniks.se19.team_g.project_rbsg.model.User;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.server.rest.JoinGameManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Component
@Scope("prototype")
public class Bot extends Thread {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final ApplicationContext applicationContext;
    private User user;
    private Game gameData;

    private final JoinGameManager joinGameManager;
    private final ObjectProvider<IngameContext> contextFactory;

    final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

    private final CompletableFuture<Bot> bootPromise = new CompletableFuture<>();
    private final CompletableFuture<Bot> closePromise = new CompletableFuture<>();
    private String botName;
    private IngameContext ingameContext;

    public Bot(
            ApplicationContext applicationContext,
            JoinGameManager joinGameManager,
            @Qualifier("dedicatedContext") ObjectProvider<IngameContext> contextFactory
    ) {
        this.applicationContext = applicationContext;
        this.joinGameManager = joinGameManager;
        this.contextFactory = contextFactory;
    }

    public Bot start(Game gameData, User user) {

        this.user = user;
        this.gameData = gameData;
        super.start();
        return this;
    }

    @Override
    public void run() {
        setupThread();

        botName = "bot@" + user.getName();

        executor.setCorePoolSize(1);
        executor.setThreadNamePrefix(botName);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();

        CompletableFuture<IngameContext> joinGamePromise = CompletableFuture
                .runAsync( () -> joinGameManager.doJoinGame(user, gameData), executor)
                .thenApply(aVoid -> contextFactory.getObject(user, gameData))
                .thenApply(ingameContext -> {
                    setIngameContext(ingameContext);
                    ingameContext.boot(false);
                    return ingameContext;
                })
        ;

        CompletableFuture<String> createArmiesPromise = CompletableFuture.completedFuture("testArmy");

        CompletableFuture.allOf(joinGamePromise, createArmiesPromise)
                .thenRun(() -> bootPromise.complete(this))
        ;

        bootPromise.thenRunAsync(this::beABot, executor);

        closePromise.thenRunAsync(this::shutdown, executor)
            .thenRun(executor::shutdown)
            .thenRun(() -> logger.debug(botName + " says bye bye!"))
        ;
    }

    private void shutdown() {
        ingameContext.getGameEventManager().terminate();
    }

    private void setIngameContext(IngameContext ingameContext) {
        this.ingameContext = ingameContext;
    }

    private void beABot() {
        logger.debug("being a bot, doing bot stuff");


        CompletableFuture.runAsync(
                () -> {ingameContext.getGameEventManager().terminate();},
            CompletableFuture.delayedExecutor(5, TimeUnit.SECONDS)
        ).thenRunAsync(
                () -> {},
                CompletableFuture.delayedExecutor(25, TimeUnit.SECONDS)
        ).thenRun( () -> closePromise.complete(this));
    }

    private void setupThread() {
        UserContextHolder.setContext(new UserContext());
        applicationContext.getBean(UserProvider.class).set(user);
    }

    public CompletableFuture<Bot> getBootPromise() {
        return bootPromise;
    }
}
