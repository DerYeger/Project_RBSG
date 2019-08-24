package de.uniks.se19.team_g.project_rbsg.skynet.behaviour.movement;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Cell;
import org.springframework.lang.NonNull;

public class AdvancedMovementTargetEvaluator implements MovementTargetEvaluator {

    @Override
    public int compare(@NonNull final MovementTarget first,
                       @NonNull final MovementTarget second) {
        if (first.distance < second.distance) {
            return -1;
        } else if (first.distance > second.distance) {
            return 1;
        } else {
            return occupiedNeighbors(first.enemyPosition) - occupiedNeighbors(second.enemyPosition);
        }
    }

    private int occupiedNeighbors(@NonNull final Cell cell) {
        return (int) cell
                .getNeighbors()
                .stream()
                .filter(neighbor -> neighbor.getUnit() != null)
                .count();
    }
}