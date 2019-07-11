package de.uniks.se19.team_g.project_rbsg.ingame.waiting_room.model;

import org.springframework.lang.NonNull;

/**
 * @author Jan Müller
 */
public enum UnitType {
    BAZOOKA_TROOPER("Bazooka Trooper"),
    CHOPPER("Chopper"),
    HEAVY_TANK("Heavy Tank"),
    INFANTRY("Infantry"),
    JEEP("Jeep"),
    LIGHT_TANK("Light Tank");

    private final String type;

    UnitType(@NonNull final String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
