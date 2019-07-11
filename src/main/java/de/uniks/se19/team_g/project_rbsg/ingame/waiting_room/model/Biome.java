package de.uniks.se19.team_g.project_rbsg.ingame.waiting_room.model;

import org.springframework.lang.NonNull;

/**
 * @author Jan Müller
 */
public enum Biome {
    FOREST("Forest"),
    GRASS("Grass"),
    MOUNTAIN("Mountain"),
    WATER("Water");

    private final String biome;

    Biome(@NonNull final String biome) {
        this.biome = biome;
    }

    @Override
    public String toString() {
        return biome;
    }
}
