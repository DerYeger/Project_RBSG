package de.uniks.se19.team_g.project_rbsg.waiting_room.model.util;

import com.fasterxml.jackson.databind.JsonNode;
import de.uniks.se19.team_g.project_rbsg.waiting_room.model.Game;
import de.uniks.se19.team_g.project_rbsg.waiting_room.model.ModelManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

/**
 * @author Jan Müller
 */
public class GameUtil {

    private static final String ALL_PLAYER = "allPlayer";
    private static final String ALL_UNITS = "allUnits";

    private static final Logger LOGGER = LoggerFactory.getLogger(GameUtil.class);

    public static Game buildGame(@NonNull final ModelManager modelManager,
                                 @NonNull final String identifier,
                                 @NonNull final JsonNode data,
                                 @NonNull final boolean logging) {
        final Game game = modelManager.gameWithId(identifier);

        if (data.has(ALL_PLAYER)) {
            final JsonNode players = data.get(ALL_PLAYER);
            if (players.isArray()) {
                for (final JsonNode player : players) {
                    game.withPlayer(modelManager.playerWithId(player.asText()));
                }
            }
        }

        if (data.has(ALL_UNITS)) {
            final JsonNode units = data.get(ALL_UNITS);
            if (units.isArray()) {
                for (final JsonNode unit : units) {
                    game.withUnit(modelManager.unitWithId(unit.asText()));
                }
            }
        }

        if (logging) LOGGER.debug("Added game: " + game);

        return game;
    }
}
