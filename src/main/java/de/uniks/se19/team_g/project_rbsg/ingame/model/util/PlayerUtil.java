package de.uniks.se19.team_g.project_rbsg.ingame.model.util;

import com.fasterxml.jackson.databind.JsonNode;
import de.uniks.se19.team_g.project_rbsg.ingame.model.ModelManager;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

public class PlayerUtil {

    @NonNull
    private static final Logger logger = LoggerFactory.getLogger(PlayerUtil.class);

    public static Player buildPlayer(@NonNull final ModelManager modelManager,
                                     @NonNull final String identifier,
                                     @NonNull final JsonNode data,
                                     @NonNull final boolean debug) {
        final Player player = modelManager.playerWithId(identifier);

        if (data.has("currentGame")) player.setGame(modelManager.gameWithId(data.get("currentGame").asText()));
        if (data.has("name")) player.setName(data.get("name").asText());
        if (data.has("color")) player.setColor(data.get("color").asText());

        if (data.has("army")) {
            final JsonNode army = data.get("army");
            if (army.isArray()) {
                for (final JsonNode unit : army) {
                    player.withUnit(modelManager.unitWithId(unit.asText()));
                }
            }
        }

        if (debug) logger.debug("Added player: " + player);

        return player;
    }
}
