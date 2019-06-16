package de.uniks.se19.team_g.project_rbsg.waiting_room.model.util;

import com.fasterxml.jackson.databind.JsonNode;
import de.uniks.se19.team_g.project_rbsg.waiting_room.model.ModelManager;
import de.uniks.se19.team_g.project_rbsg.waiting_room.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

/**
 * @author Jan Müller
 */
public class PlayerUtil {

    private static final String CURRENT_GAME = "currentGame";
    private static final String NAME = "name";
    private static final String COLOR = "color";
    private static final String ARMY = "army";

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerUtil.class);

    public static Player buildPlayer(@NonNull final ModelManager modelManager,
                                     @NonNull final String identifier,
                                     @NonNull final JsonNode data,
                                     @NonNull final boolean logging) {
        final Player player = modelManager.playerWithId(identifier);

        if (data.has(CURRENT_GAME)) player.setGame(modelManager.gameWithId(data.get(CURRENT_GAME).asText()));
        if (data.has(NAME)) player.setName(data.get(NAME).asText());
        if (data.has(COLOR)) player.setColor(data.get(COLOR).asText());

        if (data.has(ARMY)) {
            final JsonNode army = data.get(ARMY);
            if (army.isArray()) {
                for (final JsonNode unit : army) {
                    player.withUnit(modelManager.unitWithId(unit.asText()));
                }
            }
        }

        if (logging) LOGGER.debug("Added player: " + player);

        return player;
    }
}