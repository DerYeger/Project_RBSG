package de.uniks.se19.team_g.project_rbsg.ingame.model.util;

import com.fasterxml.jackson.databind.JsonNode;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Biome;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Cell;
import de.uniks.se19.team_g.project_rbsg.ingame.model.ModelManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

/**
 * @author Jan Müller
 */
public class CellUtil {

    private static final String GAME = "game";
    private static final String IS_PASSABLE = "value";
    private static final String X = "x";
    private static final String Y = "y";
    private static final String LEFT = "left";
    private static final String TOP = "top";
    private static final String RIGHT = "right";
    private static final String BOTTOM = "bottom";

    private static final Logger LOGGER = LoggerFactory.getLogger(CellUtil.class);

    public static Cell buildCell(@NonNull final ModelManager modelManager,
                                 @NonNull final String identifier,
                                 @NonNull final Biome biome,
                                 @NonNull final JsonNode data,
                                 @NonNull final boolean logging) {
        final Cell cell = modelManager.cellWithId(identifier).setBiome(biome);

        if (data.has(GAME)) cell.setGame(modelManager.gameWithId(data.get(GAME).asText()));
        if (data.has(IS_PASSABLE)) cell.setPassable(data.get(IS_PASSABLE).asBoolean());
        if (data.has(X)) cell.setX(data.get(X).asInt());
        if (data.has(Y)) cell.setY(data.get(Y).asInt());

        final Cell left = cellInDirection(modelManager, LEFT, data);
        final Cell top = cellInDirection(modelManager, TOP,  data);
        final Cell right = cellInDirection(modelManager, RIGHT,  data);
        final Cell bottom = cellInDirection(modelManager, BOTTOM, data);

        cell.setLeft(left)
                .setTop(top)
                .setRight(right)
                .setBottom(bottom);

        if (logging) LOGGER.debug("Added cell:" + cell);

        return cell;
    }

    private static Cell cellInDirection(@NonNull final ModelManager modelManager,
                                        @NonNull final String direction,
                                        @NonNull final JsonNode data) {
        if (!data.has(direction)) return null;
        return modelManager.cellWithId(data.get(direction).asText());
    }
}
