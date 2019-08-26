package de.uniks.se19.team_g.project_rbsg.skynet.behaviour.movement;

import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.MovementEvaluator;
import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.Tour;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Cell;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Game;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;
import de.uniks.se19.team_g.project_rbsg.skynet.action.MovementAction;
import de.uniks.se19.team_g.project_rbsg.skynet.behaviour.Behaviour;
import de.uniks.se19.team_g.project_rbsg.skynet.behaviour.BehaviourException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import java.util.*;
import java.util.stream.Collectors;

public class MovementBehaviour implements Behaviour {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final MovementEvaluator movementEvaluator = new MovementEvaluator();
    private MovementTargetEvaluator movementTargetEvaluator;

    public void setMovementTargetEvaluator(@NonNull final MovementTargetEvaluator movementTargetEvaluator) {
        this.movementTargetEvaluator = movementTargetEvaluator;
    }

    @Override
    public Optional<MovementAction> apply(@NonNull final Game game,
                                  @NonNull final Player player) {
        try {
            if (movementTargetEvaluator == null) movementTargetEvaluator = new AdvancedMovementTargetEvaluator();
            final var unit = getMovableUnitWithTarget(player);
            final var allowedTours = movementEvaluator.getValidTours(unit);
            final var target = getOptimalTarget(getEnemies(unit), allowedTours);

            return Optional.of(new MovementAction(unit, allowedTours.get(target)));
        } catch (final BehaviourException e) {
            logger.info(e.getMessage());
        }
        return Optional.empty();
    }

    private Unit getMovableUnitWithTarget(@NonNull final Player player) throws MovementBehaviourException {
        return player
                .getUnits()
                .stream()
                .filter(unit -> isMovableUnit(unit) && hasTarget(unit))
                .findFirst()
                .orElseThrow(() -> new MovementBehaviourException("No movable unit or target found"));
    }

    private boolean isMovableUnit(@NonNull final Unit unit) {
        return unit.getRemainingMovePoints() > 0
                && unit
                        .getNeighbors()
                        .stream()
                        .noneMatch(unit::canAttack);
    }

    private boolean hasTarget(@NonNull final Unit unit) {
        return unit
                .getGame()
                .getUnits()
                .stream()
                .anyMatch(other -> isATarget(unit, other));
    }

    private boolean isATarget(@NonNull final Unit unit,
                              @NonNull final Unit other) {
        return unit.canAttack(other) && other.getNeighbors().size() < 4;
    }

    private ArrayList<Enemy> getEnemies(@NonNull final Unit unit) throws MovementBehaviourException {
        final var enemyPositions = unit
                .getGame()
                .getUnits()
                .stream()
                .filter(other -> isATarget(unit, other))
                .map(other -> new Enemy(other, threatLevel(other, unit.getLeader())))
                .collect(Collectors.toCollection(ArrayList::new));
        if (enemyPositions.size() < 1) throw new MovementBehaviourException("An unexpected error occurred");
        return enemyPositions;
    }

    private double threatLevel(@NonNull final Unit enemy,
                               @NonNull final Player me) {
        return me
                .getUnits()
                .stream()
                .mapToDouble(enemy::getAttackValue)
                .average()
                .orElse(0);
    }

    private Cell getOptimalTarget(@NonNull final ArrayList<Enemy> enemies,
                                  @NonNull final Map<Cell, Tour> allowedTours) throws MovementBehaviourException {
        return allowedTours
                .keySet()
                .stream()
                .map(cell -> toTarget(cell, enemies))
                .filter(Objects::nonNull)
                .min(movementTargetEvaluator)
                .orElseThrow(() -> new MovementBehaviourException("Unable to determine optimal target"))
                .cell;
    }

    private MovementTarget toTarget(@NonNull final Cell cell,
                                    @NonNull final ArrayList<Enemy> enemies) {
        return enemies
                .stream()
                .map(enemy -> new MovementTarget(cell, enemy, distance(cell, enemy.position)))
                .min(Comparator.comparingDouble(target -> target.distance))
                .orElse(null);
    }

    private Double distance(@NonNull final Cell first,
                            @NonNull final Cell second) {
        return Math.sqrt(
                Math.pow(first.getX() - second.getX(), 2)
                        + Math.pow(first.getY() - second.getY(), 2));
    }
}
