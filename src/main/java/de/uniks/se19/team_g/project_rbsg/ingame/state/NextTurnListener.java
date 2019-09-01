package de.uniks.se19.team_g.project_rbsg.ingame.state;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Game;
import de.uniks.se19.team_g.project_rbsg.ingame.model.ModelManager;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

@Component
public class NextTurnListener implements GameEventDispatcher.Listener {

    private Logger logger = LoggerFactory.getLogger(getClass());

    // it's fine. we initialize them with expectNextTurn
    @SuppressWarnings("NullableProblems")
    @Nonnull
    private CompletableFuture<ModelManager> phaseExpectation;
    @SuppressWarnings("NullableProblems")
    @Nonnull
    private CompletableFuture<Player> playerExpectation;
    @SuppressWarnings("NullableProblems")
    @Nonnull
    private CompletableFuture<Void> combined;

    public NextTurnListener(){
        expectNextTurn();
    }

    private void expectNextTurn() {
        phaseExpectation = new CompletableFuture<>();
        playerExpectation = new CompletableFuture<>();

        combined = phaseExpectation.thenCombine(
            playerExpectation,
            this::publishNextTurn
        );
        combined
            .exceptionally(throwable -> {logger.error("failed waiting for next player event", throwable); return null;})
            .thenRun(this::expectNextTurn);
    }

    private Void publishNextTurn(ModelManager modelManager, Player player) {
        modelManager.addAction(
                new NextTurnAction(modelManager.getGame(), player)
        );

        return null;
    }

    @Override
    public void accept(GameEvent gameEvent, GameEventDispatcher dispatcher) {

        if(!(gameEvent instanceof  GameChangeObjectEvent)){
            return;
        }

        GameChangeObjectEvent changeEvent = (GameChangeObjectEvent) gameEvent;
        Object entity = dispatcher.getModelManager().getEntityById(changeEvent.getEntityId());

        if (!(entity instanceof Game)) {
            return;
        }
        Game game = (Game) entity;

        if(isFirstMovementPhaseEvent(changeEvent)) {
            if (phaseExpectation.isDone()) {
                combined.completeExceptionally(new IllegalStateException("received multiple times in a row firstMovementPhase event without a player change"));
            }
            phaseExpectation.complete(dispatcher.getModelManager());
        }

        if (isNextPlayerEvent(changeEvent)) {

            Object player = dispatcher.getModelManager().getEntityById(changeEvent.getNewValue());
            if (!(player instanceof Player)) {
                // doesn't make sense
                logger.error("received unknown player id as next player");
                return;
            }
            if (playerExpectation.isDone()) {
                combined.completeExceptionally(new IllegalStateException("received multiple times in a row firstMovementPhase event without a player change"));
            }
            playerExpectation.complete((Player) player);
        }
    }

    private boolean isNextPlayerEvent(GameChangeObjectEvent changeEvent) {
        return "currentPlayer".equals(changeEvent.getFieldName());
    }

    private boolean isFirstMovementPhaseEvent(GameChangeObjectEvent changeEvent) {
        return
            "phase".equals(changeEvent.getFieldName()) &&
            Game.Phase.movePhase.name().equals(changeEvent.getNewValue())
        ;
    }
}
