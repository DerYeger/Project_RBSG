package de.uniks.se19.team_g.project_rbsg.ingame.model;

import org.springframework.lang.NonNull;

public enum Type {
    BAZOOKA("Bazooka"),
    CHOPPER("Chopper"),
    HEAVY_TANK("Heavy Tank"),
    INFANTRY("Infantry"),
    JEEP("Jeep"),
    LIGHT_TANK("Light Tank");

    private final String type;

    Type(@NonNull final String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
