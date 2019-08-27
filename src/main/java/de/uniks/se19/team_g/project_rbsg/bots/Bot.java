package de.uniks.se19.team_g.project_rbsg.bots;

import de.uniks.se19.team_g.project_rbsg.configuration.army.ArmyGeneratorStrategy;
import de.uniks.se19.team_g.project_rbsg.ingame.IngameContext;
import de.uniks.se19.team_g.project_rbsg.model.Army;
import de.uniks.se19.team_g.project_rbsg.model.Game;
import de.uniks.se19.team_g.project_rbsg.model.User;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.server.rest.JoinGameManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Component
@Scope("prototype")
public class Bot extends Thread {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private User user;
    private Game gameData;

    private final JoinGameManager joinGameManager;
    private final ObjectProvider<IngameContext> contextFactory;

    final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

    private final CompletableFuture<Bot> bootPromise = new CompletableFuture<>();
    private final CompletableFuture<Bot> closePromise = new CompletableFuture<>();
    private final CompletableFuture<Void> shutdownPromise = new CompletableFuture<>();
    private IngameContext ingameContext;
    private UserProvider userProvider;
    private ArmyGeneratorStrategy armyGeneratorStrategy;

    public Bot(
            UserProvider userProvider,
            JoinGameManager joinGameManager,
            @Qualifier("dedicatedContext") ObjectProvider<IngameContext> contextFactory
    ) {
        this.userProvider = userProvider;
        this.joinGameManager = joinGameManager;
        this.contextFactory = contextFactory;
    }

    public Bot start(Game gameData, User user) {

        this.user = user;
        this.gameData = gameData;
        super.start();
        return this;
    }

    public void setArmyGeneratorStrategy(ArmyGeneratorStrategy armyGeneratorStrategy) {
        this.armyGeneratorStrategy = armyGeneratorStrategy;
    }


    @Override
    public void run() {
        setupThread();

        setName("bot@" + user.getName());

        executor.setCorePoolSize(1);
        executor.setThreadNamePrefix(getName());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();

        closePromise.thenRun(this::shutdown);

        boot();

        try {
            // wait until shutdown
            shutdownPromise.get();
        } catch (InterruptedException | ExecutionException e) {
            logger.error("exception while waiting on shutdown hook", e);
        }
    }

    protected void boot() {
        // join game via rest and websocket
        CompletableFuture<IngameContext> joinGamePromise = CompletableFuture
                .runAsync( () -> joinGameManager.doJoinGame(user, gameData), executor)
                .thenApply(aVoid -> contextFactory.getObject(user, gameData))
                .thenApply(ingameContext -> {
                    setIngameContext(ingameContext);
                    ingameContext.getModelManager().setExecutor(executor);
                    ingameContext.boot(false);
                    return ingameContext;
                })
        ;

        // in parallele, create a army
        CompletableFuture<Army> armyGeneration = CompletableFuture
                .supplyAsync( this::createArmy, executor);

        // when game joined and army created -> select army & ready
        CompletableFuture<Bot> botReady = joinGamePromise
                .thenCombineAsync(
                    armyGeneration,
                    this::selectArmy,
                    executor
                )
                // wait a tad bit before giving ready
                .thenApplyAsync(
                    context -> {context.getGameEventManager().api().ready(); return context;},
                    CompletableFuture.delayedExecutor(50, TimeUnit.MILLISECONDS, executor)
                ).thenApply(
                    any -> this
                );

        // when everything is ready
        botReady
                .thenRun(this::beABot)
                .thenRun(() -> bootPromise.complete(this))
                .exceptionally(ex -> { bootPromise.completeExceptionally(ex); return null;})
        ;
    }

    private IngameContext selectArmy(IngameContext context, Army army) {
        context.getGameEventManager().api().selectArmy(army);
        return context;
    }

    @Nonnull
    protected Army createArmy() {
        Army army = armyGeneratorStrategy.createArmy(null);
        army.id.set("testArmy");
        return army;
    }

    public void shutdown() {
        CompletableFuture.runAsync(this::doShutdown, executor)
                .exceptionally(throwable -> {logger.error("shutdown failed", throwable); return null;})
                .thenRun(executor::shutdown)
                .thenRun(() -> logger.debug(getName() + " says bye bye!"))
                .thenRun(() -> shutdownPromise.complete(null))
        ;
    }

    private void doShutdown() {
        ingameContext.getGameEventManager().terminate();
    }

    private void setIngameContext(IngameContext ingameContext) {
        this.ingameContext = ingameContext;
    }

    private void beABot() {
        ingameContext.initializedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                logger.debug("my game was initialized");
            }
            ingameContext.getGameState().getPlayers().forEach(
                    player -> {
                        player.isReadyProperty().addListener((observable1, oldValue1, newValue1) -> {
                            logger.debug( player + " is" + (newValue ? "" : " not") + " ready");
                        });
                    }
            );
        });

        CompletableFuture
                .runAsync(() -> {
                    logger.debug("being a bot, doing bot stuff");
                }, executor)
                .thenRunAsync(
                        () -> {ingameContext.getGameEventManager().terminate();},
                    CompletableFuture.delayedExecutor(10, TimeUnit.SECONDS, executor)
                ).thenRunAsync(
                        () -> {},
                        CompletableFuture.delayedExecutor(2, TimeUnit.SECONDS, executor)
                ).thenRun( () -> closePromise.complete(this))
        ;
    }

    private void setupThread() {
        UserContextHolder.setContext(new UserContext());
        userProvider.set(user);
    }

    public CompletableFuture<Bot> getBootPromise() {
        return bootPromise;
    }
}
