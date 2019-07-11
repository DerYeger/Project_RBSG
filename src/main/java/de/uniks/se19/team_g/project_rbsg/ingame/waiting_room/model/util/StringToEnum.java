package de.uniks.se19.team_g.project_rbsg.ingame.waiting_room.model.util;

import de.uniks.se19.team_g.project_rbsg.ingame.waiting_room.model.Biome;
import de.uniks.se19.team_g.project_rbsg.ingame.waiting_room.model.UnitType;
import org.springframework.lang.NonNull;

/**
 * @author Jan Müller
 */
public class StringToEnum {

    public static Biome biome(@NonNull final String string) {
        return Biome.valueOf(string.toUpperCase().replace(" ", "_"));
    }

    public static UnitType unitType(@NonNull final String string) {
        return UnitType.valueOf(string.toUpperCase().replace(" ", "_"));
    }
}
