package de.uniks.se19.team_g.project_rbsg.ingame.model.util;

import com.fasterxml.jackson.databind.JsonNode;
import de.uniks.se19.team_g.project_rbsg.configuration.flavor.UnitTypeInfo;
import de.uniks.se19.team_g.project_rbsg.ingame.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import java.util.ArrayList;

/**
 * @author Jan Müller
 */
public class UnitUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnitUtil.class);

    private static final String TYPE = "type";
    private static final String MP = "mp";
    private static final String HP = "hp";
    private static final String GAME = "game";
    private static final String LEADER = "leader";
    private static final String POSITION = "position";
    private static final String CAN_ATTACK = "canAttack";

    @SuppressWarnings("UnusedReturnValue")
    public static Unit buildUnit(@NonNull final ModelManager modelManager,
                                 @NonNull final String identifier,
                                 @NonNull final JsonNode data,
                                 @NonNull final boolean logging) {
        final Unit unit = modelManager.unitWithId(identifier);

        if (data.has(TYPE)) unit.setUnitType(UnitTypeInfo.resolveType(data.get(TYPE).asText()));
        if (data.has(MP)) unit.setMp(data.get(MP).asInt());
        if (data.has(HP))  {
            unit.setHp(data.get(HP).asInt());
            unit.setMaxHp(data.get(HP).asInt());
        }
        if (data.has(GAME)) unit.setGame(modelManager.gameWithId(data.get(GAME).asText()));
        if (data.has(LEADER)) unit.setLeader(modelManager.playerWithId(data.get(LEADER).asText()));
        if (data.has(POSITION)) unit.setPosition(modelManager.cellWithId(data.get(POSITION).asText()));

        if (data.has(CAN_ATTACK)) {
            final JsonNode unitTypes = data.get(CAN_ATTACK);
            if (unitTypes.isArray()) {
                final ArrayList<UnitTypeInfo> canAttack = new ArrayList<>();
                for (final JsonNode unitType : unitTypes) {
                    canAttack.add(UnitTypeInfo.resolveType(unitType.asText()));
                }
                unit.setCanAttack(canAttack);
            }
        }

        if (logging) LOGGER.debug("Added unit: " + unit);

        return unit;
    }

    private static final String GAME_UNITS = "allUnits";
    private static final String PLAYER_UNITS = "army";
    private static final String CELL = "blockedBy";

    public static void removeUnitFrom(@NonNull final ModelManager modelManager,
                                      @NonNull final String identifier,
                                      @NonNull final String from,
                                      @NonNull final String fieldName,
                                      @NonNull final boolean logging) {
        final Unit unit = modelManager.unitWithId(identifier);

        switch (fieldName) {
            case GAME_UNITS:
                final Game game = modelManager.gameWithId(from);
                if (unit.getGame() != null && unit.getGame().equals(game)) unit.setGame(null);
                break;
            case PLAYER_UNITS:
                final Player player = modelManager.playerWithId(from);
                if (unit.getLeader() != null && unit.getLeader().equals(player)) unit.setLeader(null);
                break;
            case CELL:
                final Cell cell = modelManager.cellWithId(from);
                if (unit.positionProperty().get() != null && unit.positionProperty().get().equals(cell)) unit.setPosition(null);
                break;
            default:
                LOGGER.error("Unknown fieldName for " + from + ": " + fieldName);
                return;
        }

        if (logging) LOGGER.debug(identifier + " removed from field " + fieldName + " from Object " + from);
    }
}
