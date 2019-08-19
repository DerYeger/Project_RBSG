package de.uniks.se19.team_g.project_rbsg.skynet.behaviour;

import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.MovementEvaluator;
import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.Tour;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Cell;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Game;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;
import de.uniks.se19.team_g.project_rbsg.skynet.action.MovementAction;
import de.uniks.se19.team_g.project_rbsg.skynet.behaviour.exception.BehaviourException;
import de.uniks.se19.team_g.project_rbsg.skynet.behaviour.exception.MovementBehaviourException;
import de.uniks.se19.team_g.project_rbsg.util.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import java.util.*;
import java.util.stream.Collectors;

public class MovementBehaviour implements Behaviour {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final MovementEvaluator movementEvaluator = new MovementEvaluator();

    @Override
    public Optional<MovementAction> apply(@NonNull final Game game,
                                  @NonNull final Player player) {
        try {
            final Unit unit = getFirstUnitWithRemainingMP(player);
            final Map<Cell, Tour> allowedTours = movementEvaluator.getValidTours(unit);

            final Cell target = getOptimalTarget(getTargets(unit), allowedTours);

            return Optional.of(new MovementAction(unit, allowedTours.get(target)));
        } catch (final BehaviourException e) {
            logger.info(e.getMessage());
        }
        return Optional.empty();
    }

    private Unit getFirstUnitWithRemainingMP(@NonNull final Player player) throws MovementBehaviourException {
        return player
                .getUnits()
                .stream()
                .filter(unit -> isMovableUnit(unit) && hasTarget(unit))
                .findFirst()
                .orElseThrow(() -> new MovementBehaviourException("No movable unit or target found"));
    }

    private boolean isMovableUnit(@NonNull final Unit unit) {
        return unit.getRemainingMovePoints() > 0 &&
                unit
                        .getNeighbors()
                        .stream()
                        .noneMatch(unit::canAttack);
    }

    private boolean hasTarget(@NonNull final Unit unit) {
        return unit
                .getGame()
                .getUnits()
                .stream()
                .anyMatch(other -> unit.canAttack(other) && other.getNeighbors().size() < 4);
    }

    private ArrayList<Cell> getTargets(@NonNull final Unit unit) throws MovementBehaviourException {
        final ArrayList<Cell> enemyPositions = unit
                .getGame()
                .getUnits()
                .stream()
                .filter(other -> unit.canAttack(other) && other.getNeighbors().size() < 4)
                .map(Unit::getPosition)
                .collect(Collectors.toCollection(ArrayList::new));
        if (enemyPositions.size() < 1) throw new MovementBehaviourException("An unexpected error occurred");
        return enemyPositions;
    }

    private Cell getOptimalTarget(@NonNull final ArrayList<Cell> enemyPositions,
                                  @NonNull final Map<Cell, Tour> allowedTours) throws MovementBehaviourException {
        return allowedTours
                .keySet()
                .stream()
                .map(cell -> new Tuple<>(cell, smallestDistance(cell, enemyPositions)))
                .filter(pair -> pair.second > 0)
                .min(Comparator.comparingDouble(pair -> pair.second))
                .orElseThrow(() -> new MovementBehaviourException("No target cell found"))
                .first;
    }

    private Double smallestDistance(@NonNull final Cell cell,
                                    @NonNull final ArrayList<Cell> enemyPositions) {
        return enemyPositions
                .stream()
                .mapToDouble(other -> evaluation(cell, other))
                .min()
                .orElse(0);
    }

    private double evaluation(@NonNull final Cell first,
                              @NonNull final Cell second) {
        return Math.pow(distance(first, second), 2) + second.getUnit().getNeighbors().size();
    }

    private double distance(@NonNull final Cell first,
                            @NonNull final Cell second) {
        return Math.sqrt(
                Math.pow(first.getX() - second.getX(), 2)
                        + Math.pow(first.getY() - second.getY(), 2));
    }
}
