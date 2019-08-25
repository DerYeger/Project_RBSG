package de.uniks.se19.team_g.project_rbsg.skynet.behaviour.movement;

import org.springframework.lang.NonNull;

public class SimpleMovementTargetEvaluator implements MovementTargetEvaluator {

    @Override
    public int compare(@NonNull final MovementTarget first,
                       @NonNull final MovementTarget second) {
        if (first == second) return 0;
        return (int) (first.distance - second.distance);
    }
}
