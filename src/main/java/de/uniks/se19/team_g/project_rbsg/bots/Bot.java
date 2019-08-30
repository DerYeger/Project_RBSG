package de.uniks.se19.team_g.project_rbsg.bots;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.uniks.se19.team_g.project_rbsg.chat.ChatClient;
import de.uniks.se19.team_g.project_rbsg.configuration.army.ArmyGeneratorStrategy;
import de.uniks.se19.team_g.project_rbsg.ingame.IngameContext;
import de.uniks.se19.team_g.project_rbsg.ingame.event.GameEventManager;
import de.uniks.se19.team_g.project_rbsg.model.Army;
import de.uniks.se19.team_g.project_rbsg.model.Game;
import de.uniks.se19.team_g.project_rbsg.model.User;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.server.rest.JoinGameManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.persistance.CreateArmyService;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.persistance.serverResponses.SaveArmyResponse;
import de.uniks.se19.team_g.project_rbsg.skynet.Skynet;
import de.uniks.se19.team_g.project_rbsg.skynet.action.ActionExecutor;
import de.uniks.se19.team_g.project_rbsg.skynet.behaviour.SurrenderBehaviour;
import de.uniks.se19.team_g.project_rbsg.skynet.behaviour.attack.AttackBehaviour;
import de.uniks.se19.team_g.project_rbsg.skynet.behaviour.movement.MovementBehaviour;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

@Component
@Scope("prototype")
public class Bot extends Thread {

    public static final int FREQUENCY = 666;
    private Logger logger = LoggerFactory.getLogger(getClass());

    private User user;
    private Game gameData;

    private final JoinGameManager joinGameManager;
    private final ObjectProvider<IngameContext> contextFactory;
    private final CreateArmyService createArmyService;

    final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    private Executor delayedExecutor = CompletableFuture
            .delayedExecutor(FREQUENCY, TimeUnit.MILLISECONDS, executor);

    private final CompletableFuture<Bot> bootPromise = new CompletableFuture<>();
    private final CompletableFuture<Bot> closePromise = new CompletableFuture<>();
    private final CompletableFuture<Void> shutdownPromise = new CompletableFuture<>();
    private final CompletableFuture<Void> gameStart = new CompletableFuture<>();

    private boolean running = true;

    private IngameContext ingameContext;
    private UserProvider userProvider;
    private ArmyGeneratorStrategy armyGeneratorStrategy;
    private Skynet skynet;

    public Bot(
            @Nonnull UserProvider userProvider,
            @Nonnull JoinGameManager joinGameManager,
            @Nonnull @Qualifier("dedicatedContext") ObjectProvider<IngameContext> contextFactory,
            @Nonnull CreateArmyService createArmyService
    ) {
        this.userProvider = userProvider;
        this.joinGameManager = joinGameManager;
        this.contextFactory = contextFactory;
        this.createArmyService = createArmyService;
    }

    public Bot start(Game gameData, User user) {

        this.user = user;
        this.gameData = gameData;
        super.start();
        return this;
    }

    @Autowired
    public void setArmyGeneratorStrategy(
            @Qualifier("chubbyCharlesCharge") ArmyGeneratorStrategy armyGeneratorStrategy
    ) {
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
                    ingameContext.getGameEventManager().addHandler(this::listenOnGameEventsForStart);
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

        botReady.exceptionally(
                throwable -> {
                    logger.error(this + " failed booting", throwable);
                    doShutdown();
                    return null;
                }
        );

        // when everything is ready
        botReady
                .thenRun(() -> bootPromise.complete(this))
                // just wait for game started
                .thenCombine(gameStart, (aVoid, aVoid2) -> null)
                .thenRunAsync(this::beABot, executor)
                .exceptionally(ex -> { ex.printStackTrace(); return null;})
        ;
    }

    public void listenOnGameEventsForStart(ObjectNode jsonNodes) {
        if (GameEventManager.isActionType(jsonNodes, GameEventManager.GAME_STARTS)) {
            gameStart.complete(null);
        }
    }

    private IngameContext selectArmy(IngameContext context, Army army) {
        context.getGameEventManager().api().selectArmy(army);
        return context;
    }

    @Nonnull
    protected Army createArmy() {
        Army army = armyGeneratorStrategy.createArmy(null);
        SaveArmyResponse response = createArmyService.createArmy(army);

        army.id.set(response.data.id);
        return army;
    }

    public void shutdown() {
        try {
            CompletableFuture.runAsync(this::doShutdown, executor)
                    .exceptionally(throwable -> {logger.error("shutdown failed", throwable); return null;})
                    .thenRun(executor::shutdown)
                    .thenRun(() -> logger.debug(getName() + " says bye bye!"))
                    .thenRun(() -> shutdownPromise.complete(null))
            ;
        } catch (TaskRejectedException e) {
            e.printStackTrace();
        }
    }

    private void doShutdown() {
        running = false;
        if (ingameContext != null) {
            String message = "V(-,,-)V  Bye bye! :)";
            if (
                    ingameContext.isInitialized()
                    && ingameContext.getGameState() != null
                    && ingameContext.getGameState().getWinner() != null
                    && ingameContext.getGameState().getWinner() == ingameContext.getUserPlayer()
            ) {
                logger.info("Fc*k, yeah. Wreck'd 'em");
                message = "gg wp bb 👏👏👏";
            }
            if (ingameContext.getGameEventManager() != null) {
                ingameContext.getGameEventManager().sendMessage(ChatClient.CLIENT_PUBLIC_CHANNEL, null, message);
                ingameContext.getGameEventManager().terminate();
            }
        }
    }

    private void setIngameContext(IngameContext ingameContext) {
        this.ingameContext = ingameContext;
    }

    private void beABot() {
        logger.debug( this + " is going to wreck this game and all players in it");

        ActionExecutor actionExecutor = new ActionExecutor(ingameContext.getGameEventManager().api());
        de.uniks.se19.team_g.project_rbsg.ingame.model.Game gameState = ingameContext.getGameState();
        skynet = new Skynet(
            actionExecutor,
                gameState,
            ingameContext.getUserPlayer()
        );
        skynet
            .addBehaviour(new MovementBehaviour(), "movePhase", "lastMovePhase")
            .addBehaviour(new AttackBehaviour(), "attackPhase")
            .addBehaviour(new SurrenderBehaviour(), "surrender")
        ;

        gameState.winnerProperty().addListener((observable, oldValue, newValue) -> {
            logger.info(newValue + " has won the game. " + this + " is shutting down.");
            closePromise.complete(this);
        });

        ingameContext.getGameEventManager().sendMessage(
                ChatClient.CLIENT_PUBLIC_CHANNEL,
                null,
                "❤🍀🐞🎰❤"
        );

        nextTurn();
    }

    private void nextTurn() {
        CompletableFuture
                .runAsync(
                    this::doTurn, delayedExecutor
                ).exceptionally(
                    throwable -> {throwable.printStackTrace(); return null;}
                );
    }

    private void doTurn() {
        if (!running) {
            return;
        }
        skynet.turn();
        nextTurn();
    }

    private void setupThread() {
        UserContextHolder.setContext(new UserContext());
        userProvider.set(user);
    }

    public CompletableFuture<Bot> getBootPromise() {
        return bootPromise;
    }

    public ThreadPoolTaskExecutor getExecutor() {
        return executor;
    }

    public CompletableFuture<Void> getShutdownPromise() {
        return shutdownPromise;
    }
    public User getUser() {
        return user;
    }

    public Game getGameData() {
        return gameData;
    }
}
