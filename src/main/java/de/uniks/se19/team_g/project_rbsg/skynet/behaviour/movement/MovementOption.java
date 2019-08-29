package de.uniks.se19.team_g.project_rbsg.skynet.behaviour.movement;

import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.Tour;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Cell;
import org.springframework.lang.NonNull;

public class MovementOption {

    public final Tour tour;
    public final Cell destination;
    public final Enemy enemy;
    public final Double distanceToEnemy;

    public MovementOption(@NonNull final Tour tour,
                          @NonNull final Enemy enemy) {
        this.tour = tour;
        this.destination = tour.getTarget();
        this.enemy = enemy;
        this.distanceToEnemy = destination.getDistance(enemy.position);
    }
}
