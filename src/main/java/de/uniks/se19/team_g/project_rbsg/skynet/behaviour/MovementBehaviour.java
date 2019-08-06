package de.uniks.se19.team_g.project_rbsg.skynet.behaviour;

import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.MovementEvaluator;
import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.Tour;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Cell;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Game;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;
import de.uniks.se19.team_g.project_rbsg.skynet.action.Action;
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

    @Override
    public Optional<Action> apply(@NonNull final Game game,
                                  @NonNull final Player player) {
        try {
            final Unit unit = getFirstUnitWithRemainingMP(player);
            final Map<Cell, Tour> allowedTours = new MovementEvaluator().getAllowedTours(unit);

            final Cell target = getOptimalTarget(getEnemyPositions(game, player), allowedTours);

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
                .filter(this::isMovableUnit)
                .findFirst()
                .orElseThrow(() -> new MovementBehaviourException("No movable unit left"));
    }

    private boolean isMovableUnit(@NonNull final Unit unit) {
        return unit.getRemainingMovePoints() > 0 &&
                unit
                .getPosition()
                        .getNeighbors()
                        .stream()
                        .noneMatch(neighbor ->
                                neighbor.getUnit() != null
                                        && !neighbor
                                        .getUnit()
                                        .getLeader()
                                        .equals(unit.getLeader()));
    }

    private ArrayList<Cell> getEnemyPositions(@NonNull final Game game,
                                              @NonNull final Player player) throws MovementBehaviourException {
        final ArrayList<Cell> enemyPositions = game
                .getUnits()
                .stream()
                .filter(u -> !u.getLeader().equals(player))
                .map(Unit::getPosition)
                .collect(Collectors.toCollection(ArrayList::new));
        if (enemyPositions.size() < 1) throw new MovementBehaviourException("No enemy units present");
        return enemyPositions;
    }

    private Cell getOptimalTarget(@NonNull final ArrayList<Cell> enemyPositions,
                                  @NonNull final Map<Cell, Tour> allowedTours) throws MovementBehaviourException {
        return allowedTours
                .keySet()
                .stream()
                .map(cell -> new Tuple<>(cell, smallestDistance(cell, enemyPositions)))
                .filter(pair -> pair.second > 0)
                .min(Comparator.comparingDouble(p -> p.second))
                .orElseThrow(() -> new MovementBehaviourException("No target cell found"))
                .first;
    }

    private Double smallestDistance(@NonNull final Cell cell,
                               @NonNull final ArrayList<Cell> enemyPositions) {
        return enemyPositions
                .stream()
                .mapToDouble(other -> distance(cell, other))
                .min()
                .orElse(0);
    }

    private double distance(@NonNull final Cell first,
                            @NonNull final Cell second) {
        return Math.sqrt(
                Math.pow(first.getX() - second.getX(), 2)
                        + Math.pow(first.getY() - second.getY(), 2));
    }
}
