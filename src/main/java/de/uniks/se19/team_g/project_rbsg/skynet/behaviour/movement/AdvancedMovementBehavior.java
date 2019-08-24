package de.uniks.se19.team_g.project_rbsg.skynet.behaviour.movement;

import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.MovementEvaluator;
import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.Tour;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Cell;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Game;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;
import de.uniks.se19.team_g.project_rbsg.skynet.action.MovementAction;
import de.uniks.se19.team_g.project_rbsg.skynet.behaviour.BehaviourException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import java.util.*;

public class AdvancedMovementBehavior extends MovementBehaviour {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final MovementEvaluator movementEvaluator = new MovementEvaluator();
    private MovementTargetEvaluator movementTargetEvaluator;

    @Override
    public Optional<MovementAction> apply(@NonNull final Game game,
                                          @NonNull final Player player) {
        try {
            if (movementTargetEvaluator == null) movementTargetEvaluator = new AdvancedMovementTargetEvaluator();
            final var unit = getFirstUnitWithRemainingMP(player);
            final var allowedTours = movementEvaluator.getValidTours(unit);
            final var target = getOptimalTarget(getTargets(unit), allowedTours, unit);

            return Optional.of(new MovementAction(unit, allowedTours.get(target)));
        } catch (final BehaviourException e) {
            logger.info(e.getMessage());
        }
        return Optional.empty();
    }

    private Cell getOptimalTarget(@NonNull final ArrayList<Cell> enemyPositions,
                                  @NonNull final Map<Cell, Tour> allowedTours,
                                  @NonNull final Unit unitToMove) throws MovementBehaviourException {
        return allowedTours
                .keySet()
                .stream()
                //TODO: mapping Anfrage auf sinnvolle Parameter umstellen
                .map(cell -> toTarget(cell, enemyPositions, unitToMove))
                .filter(Objects::nonNull)
                //man nimmt alle eigenen anlaufbaren endzellen+kleinste-distanz-gegner-zelle und rechnet
                //kürzeste distanz aller dieser paare aus //TODO: min Anfrage auf sinnvolle Parameter umstellen
                .min(movementTargetEvaluator)
                .orElseThrow(() -> new MovementBehaviourException("Unable to determine optimal target"))
                //bekommt die endzelle die am wenigsten distanz zu einer gegnerzelle hat
                .cell;
    }

    private MovementTarget toTarget(@NonNull final Cell cell,
                                    @NonNull final ArrayList<Cell> enemyPositions,
                                    @NonNull final Unit unitToMove) {
        return enemyPositions
                .stream()
                //eine eigene anlaufbare endzelle zu jeder gegnerzelle mappen //TODO: sinnvoller machen
                .map(other -> new MovementTarget(cell, other, distance(cell, other)))
                //kleinste distanz aller gegnerzellen zu dieser einen eigenen zelle finden
                .min(Comparator.comparingDouble(target -> target.distance))
                .orElse(null);
    }

}
