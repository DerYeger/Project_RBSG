package de.uniks.se19.team_g.project_rbsg.ingame.model.util;

import com.fasterxml.jackson.databind.JsonNode;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Biome;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Cell;
import de.uniks.se19.team_g.project_rbsg.ingame.model.ModelManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

public class CellUtil {

    @NonNull
    private static final Logger logger = LoggerFactory.getLogger(CellUtil.class);

    public static Cell buildCell(@NonNull final ModelManager modelManager,
                                 @NonNull final String identifier,
                                 @NonNull final Biome biome,
                                 @NonNull final JsonNode data,
                                 @NonNull final boolean debug) {
        final Cell cell = modelManager.cellWithId(identifier).setBiome(biome);

        if (data.has("game")) cell.setGame(modelManager.gameWithId(data.get("game").asText()));
        if (data.has("isPassable")) cell.setPassable(data.get("isPassable").asBoolean());
        if (data.has("x")) cell.setX(data.get("x").asInt());
        if (data.has("y")) cell.setY(data.get("y").asInt());

        final Cell left = cellInDirection(modelManager, "left", data);
        final Cell top = cellInDirection(modelManager, "top",  data);
        final Cell right = cellInDirection(modelManager, "right",  data);
        final Cell bottom = cellInDirection(modelManager, "bottom", data);

        cell.setLeft(left)
                .setTop(top)
                .setRight(right)
                .setBottom(bottom);

        if (debug) logger.debug("Added cell:" + cell);

        return cell;
    }

    private static Cell cellInDirection(@NonNull final ModelManager modelManager,
                                        @NonNull final String direction,
                                        @NonNull final JsonNode data) {
        if (!data.has(direction)) return null;

        return modelManager.cellWithId(data.get(direction).asText());
    }
}
