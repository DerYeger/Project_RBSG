package de.uniks.se19.team_g.project_rbsg.skynet.behaviour;

import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.MovementEvaluator;
import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.Tour;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Cell;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Game;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;
import de.uniks.se19.team_g.project_rbsg.skynet.action.Action;
import de.uniks.se19.team_g.project_rbsg.skynet.action.MovementAction;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class MovementBehaviour implements Behaviour {

    @Override
    public Optional<Action> apply(@NonNull final Game game, @NonNull final Player player) {
        try {
            final Unit unit = getFirstUnitWithRemainingMP(player);
            final Map<Cell, Tour> allowedTours = new MovementEvaluator().getAllowedTours(unit);

            final Cell target = getOptimalTarget(getEnemyPositions(game, player), allowedTours);

            return Optional.of(new MovementAction(unit, allowedTours.get(target).getPath()));
        } catch (final BehaviourException ignored) {
            
        }
        return Optional.empty();
    }

    private Unit getFirstUnitWithRemainingMP(@NonNull final Player player) throws BehaviourException {
        return player
                .getUnits()
                .stream()
                .filter(u -> u.getRemainingMovePoints() > 0)
                .findFirst()
                .orElseThrow(BehaviourException::new);
    }

    private ArrayList<Cell> getEnemyPositions(@NonNull final Game game, @NonNull final Player player) throws BehaviourException {
        final ArrayList<Cell> enemyPositions = game
                .getUnits()
                .stream()
                .filter(u -> !u.getLeader().equals(player))
                .map(Unit::getPosition)
                .collect(Collectors.toCollection(ArrayList::new));
        if (enemyPositions.size() < 1) throw new BehaviourException();
        return enemyPositions;
    }

    private Cell getOptimalTarget(@NonNull final ArrayList<Cell> enemyPositions, @NonNull final Map<Cell, Tour> allowedTours) throws BehaviourException {
        final Map<Double, Cell> distanceMap = new HashMap<>();

        allowedTours
                .keySet()
                .forEach(cell -> distanceMap.put(
                        enemyPositions
                                .stream()
                                .mapToDouble(other -> distance(cell, other))
                                .sum(),
                        cell));

        final Double minDistance = distanceMap
                .keySet()
                .stream()
                .mapToDouble(d -> d)
                .min()
                .orElseThrow(BehaviourException::new);

        return distanceMap.get(minDistance);
    }

    private double distance(@NonNull final Cell first, @NonNull final Cell second) {
        return Math.sqrt(
                Math.pow(first.getX() - second.getX(), 2)
                        + Math.pow(first.getY() - second.getY(), 2));
    }
}
