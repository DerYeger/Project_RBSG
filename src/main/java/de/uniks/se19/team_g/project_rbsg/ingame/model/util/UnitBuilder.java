package de.uniks.se19.team_g.project_rbsg.ingame.model.util;

import com.fasterxml.jackson.databind.JsonNode;
import de.uniks.se19.team_g.project_rbsg.ingame.model.ModelManager;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;
import de.uniks.se19.team_g.project_rbsg.ingame.model.UnitType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import java.util.HashSet;

public class UnitBuilder {

    @NonNull
    private static final Logger logger = LoggerFactory.getLogger(UnitBuilder.class);

    public static Unit buildUnit(@NonNull final ModelManager modelManager,
                                 @NonNull final String identifier,
                                 @NonNull final JsonNode data) {
        final Unit unit = modelManager.unitWithId(identifier);

        if (data.has("type")) unit.setUnitType(UnitType.valueOf(data.get("type").asText().toUpperCase().replace(" ", "_")));
        if (data.has("mp")) unit.setMp(data.get("mp").asInt());
        if (data.has("hp")) unit.setHp(data.get("hp").asInt());
        if (data.has("game")) unit.setGame(modelManager.gameWithId(data.get("game").asText()));
        if (data.has("leader")) unit.setLeader(modelManager.playerWithId(data.get("leader").asText()));
        if (data.has("position")) unit.setPosition(modelManager.cellWithId(data.get("position").asText()));

        if (data.has("canAttack")) {
            final JsonNode unitTypes = data.get("canAttack");
            if (unitTypes.isArray()) {
                final HashSet<UnitType> canAttack = new HashSet<>();
                for (final JsonNode unitType : unitTypes) {
                    canAttack.add(UnitType.valueOf(unitType.asText().toUpperCase().replace(" ", "_")));
                }
                unit.setCanAttack(canAttack);
            }
        }

        logger.debug("Added unit: " + unit);
        return unit;
    }
}
