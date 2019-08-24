package de.uniks.se19.team_g.project_rbsg.skynet.behaviour.movement;

        import org.springframework.lang.NonNull;

public class AdvancedMovementTargetEvaluator implements MovementTargetEvaluator {

    @Override
    public int compare(@NonNull final MovementTarget first,
                       @NonNull final MovementTarget second) {
        if (first == second) return 0;
        if (!first.distance.equals(second.distance)) {
            return (int) (first.distance - second.distance);
        } else {
            return first.enemyPosition.getNeighbors().size() - second.enemyPosition.getNeighbors().size();
        }
    }
}