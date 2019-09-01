package de.uniks.se19.team_g.project_rbsg.skynet.behaviour.movement;

import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.Tour;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Cell;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;
import org.springframework.lang.NonNull;

public class MovementOption {

    public final Unit unit;

    public final Tour tour;
    public final Cell destination;

    public final Enemy enemy;
    public final int distanceToEnemy;

    public MovementOption(@NonNull final Unit unit,
                          @NonNull final Tour tour,
                          @NonNull final Enemy enemy) {
        this.unit = unit;
        this.tour = tour;
        this.destination = tour.getTarget();
        this.enemy = enemy;
        this.distanceToEnemy = destination.getDistance(enemy.position);
    }
}
