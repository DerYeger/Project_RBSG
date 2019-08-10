package de.uniks.se19.team_g.project_rbsg.skynet.behaviour;

import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.MovementEvaluator;
import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.Tour;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Game;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;
import de.uniks.se19.team_g.project_rbsg.skynet.action.FallbackAction;
import de.uniks.se19.team_g.project_rbsg.skynet.action.MovementAction;
import de.uniks.se19.team_g.project_rbsg.skynet.action.PassAction;
import de.uniks.se19.team_g.project_rbsg.skynet.behaviour.exception.FallbackBehaviourException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import java.util.Optional;

public class FallbackBehaviour implements Behaviour {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final MovementEvaluator movementEvaluator = new MovementEvaluator();

    @Override
    public Optional<FallbackAction> apply(@NonNull final Game game, @NonNull final Player player) {
        try {
            if (game.getInitiallyMoved()) {
                return Optional.of(getPassAction(game));
            } else {
                return Optional.of(getFallbackMoveAction(game, player));
            }
        } catch (final FallbackBehaviourException e) {
            logger.info(e.getMessage());
        }
        return Optional.empty();
    }

    private FallbackAction getFallbackMoveAction(@NonNull final Game game, @NonNull final Player player) throws FallbackBehaviourException {
        final Unit unit = player
                .getUnits()
                .stream()
                .filter(this::isMovable)
                .findFirst()
                .orElseThrow(() -> new FallbackBehaviourException("Move required but not possible"));
        return FallbackAction.of(new MovementAction(
                unit,
                getTour(unit)
        )).setNextAction(getPassAction(game));
    }

    private boolean isMovable(@NonNull final Unit unit) {
        return unit.getRemainingMovePoints() > 0
                && !movementEvaluator.getValidTours(unit).isEmpty();
    }

    private Tour getTour(@NonNull final Unit unit) {
        return movementEvaluator
                .getValidTours(unit)
                .entrySet()
                .iterator()
                .next()
                .getValue();
    }

    private FallbackAction getPassAction(@NonNull final Game game) {
        return FallbackAction.of(new PassAction(game));
    }
}
