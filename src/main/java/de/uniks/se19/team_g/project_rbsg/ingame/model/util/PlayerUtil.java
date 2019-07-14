package de.uniks.se19.team_g.project_rbsg.ingame.model.util;

import com.fasterxml.jackson.databind.JsonNode;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Game;
import de.uniks.se19.team_g.project_rbsg.ingame.model.ModelManager;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.lang.NonNull;

import java.util.Iterator;
import java.util.Map;

/**
 * @author Jan Müller
 */
public class PlayerUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerUtil.class);

    public static final String ARMY = "army";

    public static Player buildPlayer(@NonNull final ModelManager modelManager,
                                     @NonNull final String identifier,
                                     @NonNull final JsonNode data,
                                     @NonNull final boolean logging) {
        final Player player = modelManager.playerWithId(identifier);

        final BeanWrapper beanWrapper = new BeanWrapperImpl(player);

        final Iterator<Map.Entry<String, JsonNode>> fields = data.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();

            if (entry.getKey().equals(ARMY)) {
                buildArmy(modelManager, player, entry.getValue());
            } else if ( entry.getKey().equals("id")) {
                // ignore
            } else if ( entry.getValue().isValueNode()) {
                setValueNode(modelManager, beanWrapper, entry);
            } else {
                LOGGER.error("no handling defined for player field {}", entry.getKey());
            }
        }

        return player;
    }

    private static void setValueNode(ModelManager modelManager, BeanWrapper player, Map.Entry<String, JsonNode> entry) {
        final String valueText = entry.getValue().asText();
        Object value = modelManager.getEntityById(valueText);
        if (value == null) {
            value = valueText;
        }

        try {
            player.setPropertyValue(entry.getKey(), value);
        } catch (BeansException e) {
            LOGGER.error("couldn't set player field", e);
        }
    }

    protected static void buildArmy(@NonNull ModelManager modelManager, Player player, JsonNode army) {
        if (army.isArray()) {
            for (final JsonNode unit : army) {
                player.withUnit(modelManager.unitWithId(unit.asText()));
            }
        }
    }

    private static final String PLAYERS = "allPlayer";

    public static void removePlayerFrom(@NonNull final ModelManager modelManager,
                                        @NonNull final String identifier,
                                        @NonNull final String from,
                                        @NonNull final String fieldName,
                                        @NonNull final boolean logging) {
        final Player player = modelManager.playerWithId(identifier);

        switch (fieldName) {
            case PLAYERS:
                final Game game = modelManager.gameWithId(from);
                if (player.getCurrentGame() != null && player.getCurrentGame().equals(game)) player.setCurrentGame(null);
                break;
            default:
                LOGGER.error("Unknown fieldName for " + from + ": " + fieldName);
                return;
        }

        if (logging) LOGGER.debug(identifier + " removed from field " + fieldName + " from Object " + from);
    }
}
