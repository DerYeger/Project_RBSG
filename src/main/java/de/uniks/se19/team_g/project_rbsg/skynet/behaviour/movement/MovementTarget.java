package de.uniks.se19.team_g.project_rbsg.skynet.behaviour.movement;

import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.Tour;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Cell;
import org.springframework.lang.NonNull;

public class MovementTarget {

    public final Tour tour;
    public final Cell destination;
    public final Enemy enemy;
    public final Double distanceToEnemy;

    public MovementTarget(@NonNull final Tour tour,
                          @NonNull final Enemy enemy) {
        this.tour = tour;
        this.destination = tour.getTarget();
        this.enemy = enemy;
        this.distanceToEnemy = distance(destination, enemy.position); //TODO replace with actual remaining path distance
    }


    private Double distance(@NonNull final Cell first,
                            @NonNull final Cell second) {
        return Math.sqrt(
                Math.pow(first.getX() - second.getX(), 2)
                        + Math.pow(first.getY() - second.getY(), 2));
    }
}
