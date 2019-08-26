package de.uniks.se19.team_g.project_rbsg.skynet.behaviour.movement;

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
            return first.enemy.unit.getNeighbors().size() - second.enemy.unit.getNeighbors().size();
        }
    }
}