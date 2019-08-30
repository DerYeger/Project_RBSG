package de.uniks.se19.team_g.project_rbsg.skynet.behaviour.movement;

import org.springframework.lang.NonNull;

public class SimpleMovementOptionEvaluator implements MovementOptionEvaluator {

    @Override
    public int compare(@NonNull final MovementOption first,
                       @NonNull final MovementOption second) {
        if (first == second) return 0;
        return (int) (first.distanceToEnemy - second.distanceToEnemy);
    }
}
