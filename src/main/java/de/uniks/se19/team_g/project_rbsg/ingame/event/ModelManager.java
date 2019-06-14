package de.uniks.se19.team_g.project_rbsg.ingame.event;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Biome;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Cell;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Game;
import de.uniks.se19.team_g.project_rbsg.lobby.model.Player;
import de.uniks.se19.team_g.project_rbsg.model.*;
import de.uniks.se19.team_g.project_rbsg.util.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import java.util.HashMap;

public class ModelManager implements GameEventHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final HashMap<String, HashMap> map;

    private final Game game;

    public ModelManager() {
        map = new HashMap<>();
        game = new Game();
    }

    @Override
    public void handle(@NonNull final ObjectNode node) {
        if (!node.has("data")) return;

        final JsonNode data = node.get("data");

        if (!data.has("id")) return;

        final String identifier = data.get("id").asText();
        final Tuple<String, String> typeAndId = getTypeAndId(identifier);
        final String type = typeAndId.first;
        final String id = typeAndId.second;

        switch (type) {
            case "Game":
                handleGame();
                break;
            case "Player":
                handlePlayer(id, data);
                break;
            case "Unit":
                handleUnit(id, data);
                break;
            case "Forest":
                handleCell(id, Biome.FOREST, data);
                break;
            case "Grass":
                handleCell(id, Biome.GRASS, data);
                break;
            case "Mountain":
                handleCell(id, Biome.MOUNTAIN, data);
                break;
            case "Water":
                handleCell(id, Biome.WATER, data);
                break;
            default:
                logger.debug("Unknown class");
        }
    }

    private void handleGame(@NonNull final String id) {
        game.setId(id);
        logger.debug("Game handler not yet implemented");
    }

    private void handlePlayer(@NonNull final String id, @NonNull final JsonNode data) {
        @SuppressWarnings("unchecked")
        final HashMap<String, Player> playerMap = map.computeIfAbsent("Player", m -> new HashMap<String, Player>());
        logger.debug("Player handler not yet implemented");
    }

    private void handleUnit(@NonNull final String id, @NonNull final JsonNode data) {
        @SuppressWarnings("unchecked")
        final HashMap<String, Unit> unitMap = map.computeIfAbsent("Unit", k -> new HashMap<String, Unit>());
        logger.debug("Unit handler not yet implemented");
    }

    private void handleCell(@NonNull final String id, @NonNull final Biome biome, @NonNull final JsonNode data) {
        if (!data.has("isPassable") || !data.has("x") || !data.has("y")) {
            logger.debug("Cell information incomplete");
            return;
        }

        final HashMap<String, Cell> cellMap = game.getCells();

        final boolean isPassable = data.get("isPassable").asBoolean();

        final int x = data.get("x").asInt();
        final int y = data.get("y").asInt();

        final Cell cell = cellMap.computeIfAbsent(id, c -> new Cell(id));

        cell.setGame(game)
                .setBiome(biome)
                .setPassable(isPassable)
                .setX(x)
                .setY(y);

        final Cell left = getCellByDirection(cellMap, data, "left");
        final Cell top = getCellByDirection(cellMap, data, "top");
        final Cell right = getCellByDirection(cellMap, data, "right");
        final Cell bottom = getCellByDirection(cellMap, data, "bottom");

        cell.setLeft(left)
                .setTop(top)
                .setRight(right)
                .setBottom(bottom);
    }

    @NonNull
    private Tuple<String, String> getTypeAndId(@NonNull final String identifier) {
        final int id_index = identifier.indexOf('@');

        final String type = identifier.substring(0, id_index);

        final String id = identifier.substring(id_index + 1);

        return new Tuple<>(type, id);
    }

    private Cell getCellByDirection(@NonNull final HashMap<String, Cell> cellMap, @NonNull final JsonNode data, @NonNull final String direction) {
        if (!data.has(direction)) return null;

        final Tuple<String, String> typeAndId = getTypeAndId(data.get(direction).asText());

        return cellMap.computeIfAbsent(typeAndId.second, c -> new Cell(typeAndId.second));
    }
}
