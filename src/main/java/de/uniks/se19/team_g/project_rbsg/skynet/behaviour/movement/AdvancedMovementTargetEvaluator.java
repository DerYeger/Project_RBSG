package de.uniks.se19.team_g.project_rbsg.skynet.behaviour.movement;

import org.springframework.lang.NonNull;

public class AdvancedMovementTargetEvaluator implements MovementTargetEvaluator {

    @Override
    public int compare(@NonNull final MovementTarget first,
                       @NonNull final MovementTarget second) {
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