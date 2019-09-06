package de.uniks.se19.team_g.project_rbsg.skynet.behaviour.movement;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Cell;
import org.springframework.lang.NonNull;

public class AdvancedMovementOptionEvaluator implements MovementOptionEvaluator {

    @Override
    public int compare(@NonNull final MovementOption first,
                       @NonNull final MovementOption second) {
        if (first == second) return 0;
        if (first.distanceToEnemy < second.distanceToEnemy) {
            return -1;
        } else if (first.distanceToEnemy > second.distanceToEnemy) {
            return 1;
        } else {
            return occupiedNeighbors(first.enemy.position) - occupiedNeighbors(second.enemy.position);
        }
    }

    public int occupiedNeighbors(@NonNull final Cell cell) {
        return (int) cell
                .getNeighbors()
                .stream()
                .filter(neighbor -> neighbor.getUnit() == null)
                .count();
    }
}