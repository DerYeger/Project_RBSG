package de.uniks.se19.team_g.project_rbsg.ingame.model.util;

import com.fasterxml.jackson.databind.JsonNode;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Game;
import de.uniks.se19.team_g.project_rbsg.ingame.model.ModelManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;;

public class GameBuilder {

    @NonNull
    private static final Logger logger = LoggerFactory.getLogger(GameBuilder.class);

    public static Game buildGame(@NonNull final ModelManager modelManager,
                                 @NonNull final String identifier,
                                 @NonNull final JsonNode data) {
        final Game game = modelManager.gameWithId(identifier);

        if (data.has("allPlayer")) {
            final JsonNode players = data.get("allPlayer");
            if (players.isArray()) {
                for (final JsonNode player : players) {
                    game.withPlayer(modelManager.playerWithId(player.asText()));
                }
            }
        }

        if (data.has("allUnits")) {
            final JsonNode units = data.get("allUnits");
            if (units.isArray()) {
                for (final JsonNode unit : units) {
                    game.withUnit(modelManager.unitWithId(unit.asText()));
                }
            }
        }

        logger.debug("Added game: " + game);

        return game;
    }
}
