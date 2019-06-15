package de.uniks.se19.team_g.project_rbsg.ingame.model.util;

import com.fasterxml.jackson.databind.JsonNode;
import de.uniks.se19.team_g.project_rbsg.ingame.model.ModelManager;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;
import de.uniks.se19.team_g.project_rbsg.ingame.model.UnitType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import java.util.ArrayList;

public class UnitUtil {

    private static final String TYPE = "type";
    private static final String MP = "mp";
    private static final String HP = "hp";
    private static final String GAME = "game";
    private static final String LEADER = "leader";
    private static final String POSITION = "position";
    private static final String CAN_ATTACK = "canAttack";

    private static final Logger logger = LoggerFactory.getLogger(UnitUtil.class);

    public static Unit buildUnit(@NonNull final ModelManager modelManager,
                                 @NonNull final String identifier,
                                 @NonNull final JsonNode data,
                                 @NonNull final boolean logging) {
        final Unit unit = modelManager.unitWithId(identifier);

        if (data.has(TYPE)) unit.setUnitType(StringToEnum.unitType(data.get(TYPE).asText()));
        if (data.has(MP)) unit.setMp(data.get(MP).asInt());
        if (data.has(HP)) unit.setHp(data.get(HP).asInt());
        if (data.has(GAME)) unit.setGame(modelManager.gameWithId(data.get(GAME).asText()));
        if (data.has(LEADER)) unit.setLeader(modelManager.playerWithId(data.get(LEADER).asText()));
        if (data.has(POSITION)) unit.setPosition(modelManager.cellWithId(data.get(POSITION).asText()));

        if (data.has(CAN_ATTACK)) {
            final JsonNode unitTypes = data.get(CAN_ATTACK);
            if (unitTypes.isArray()) {
                final ArrayList<UnitType> canAttack = new ArrayList<>();
                for (final JsonNode unitType : unitTypes) {
                    canAttack.add(StringToEnum.unitType(unitType.asText()));
                }
                unit.setCanAttack(canAttack);
            }
        }

        if (logging) logger.debug("Added unit: " + unit);

        return unit;
    }
}
