package de.uniks.se19.team_g.project_rbsg.skynet.behaviour.movement;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Cell;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;
import org.springframework.lang.NonNull;

public class Enemy {

    public final Unit unit;
    public final Cell position;
    public final double threatLevel;

    public Enemy(@NonNull final Unit unit, final double threatLevel) {
        this.unit = unit;
        position = unit.getPosition();
        this.threatLevel = threatLevel;
    }
}
