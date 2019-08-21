package de.uniks.se19.team_g.project_rbsg.skynet.behaviour.movement;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Cell;
import org.springframework.lang.NonNull;

public class MovementTarget {

    public final Cell cell;
    public final Cell enemyPosition;
    public final Double distance;

    public MovementTarget(@NonNull final Cell cell,
                          @NonNull final Cell enemyPosition,
                          @NonNull final Double distance) {
        this.cell = cell;
        this.enemyPosition = enemyPosition;
        this.distance = distance;
    }
}
