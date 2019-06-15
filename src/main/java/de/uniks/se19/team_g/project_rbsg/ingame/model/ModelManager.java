package de.uniks.se19.team_g.project_rbsg.ingame.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.uniks.se19.team_g.project_rbsg.ingame.event.GameEventHandler;
import de.uniks.se19.team_g.project_rbsg.ingame.model.*;
import de.uniks.se19.team_g.project_rbsg.util.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import java.util.HashMap;

public class ModelManager implements GameEventHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final HashMap<String, Object> objectMap;

    private Game game;

    public ModelManager() {
        objectMap = new HashMap<>();
    }

    @Override
    public void handle(@NonNull final ObjectNode node) {
        if (!node.has("action")) return;

        switch (node.get("action").asText()) {
            case "gameInitObject":
                handleInit(node);
                break;
            default:
                logger.debug("Not a model message");
        }

        if (node.get("action").asText().equals("gameInitFinished")) {
            System.out.println("Game has " + game.getCells().size() + " cells");
        }
    }

    private void handleInit(@NonNull final ObjectNode node) {
        if (!node.has("data")) return;

        final JsonNode data = node.get("data");

        if (!data.has("id")) return;

        final String identifier = data.get("id").asText();
        final Tuple<String, String> typeAndId = splitIdentifier(identifier);
        final String type = typeAndId.first;

        switch (type) {
            case "Game":
                initGame(identifier, data);
                break;
            case "Player":
                initPlayer(identifier, data);
                break;
            case "Unit":
                initUnit(identifier, data);
                break;
            case "Forest":
                initCell(identifier, Biome.FOREST, data);
                break;
            case "Grass":
                initCell(identifier, Biome.GRASS, data);
                break;
            case "Mountain":
                initCell(identifier, Biome.MOUNTAIN, data);
                break;
            case "Water":
                initCell(identifier, Biome.WATER, data);
                break;
            default:
                logger.debug("Unknown class");
        }
    }

    private void initGame(@NonNull final String identifier, @NonNull final JsonNode data) {
        game = gameWithId(identifier);
        logger.debug("Game handler not yet implemented: " + data);
    }

    private void initPlayer(@NonNull final String identifier, @NonNull final JsonNode data) {
        logger.debug("Player handler not yet implemented: " + data);
    }

    private void initUnit(@NonNull final String identifier, @NonNull final JsonNode data) {
        logger.debug("Unit handler not yet implemented: " + data);
    }

    private void initCell(@NonNull final String identifier, @NonNull final Biome biome, @NonNull final JsonNode data) {
        if (!data.has("isPassable") || !data.has("x") || !data.has("y") || !data.has("game")) {
            logger.debug("Cell information incomplete");
            return;
        }

        final boolean isPassable = data.get("isPassable").asBoolean();

        final int x = data.get("x").asInt();
        final int y = data.get("y").asInt();

        final String gameIdentifier = data.get("game").asText();

        final Cell cell = cellWithId(identifier);

        cell.setGame(gameWithId(gameIdentifier))
                .setBiome(biome)
                .setPassable(isPassable)
                .setX(x)
                .setY(y);

        final Cell left = cellInDirection(data, "left");
        final Cell top = cellInDirection(data, "top");
        final Cell right = cellInDirection(data, "right");
        final Cell bottom = cellInDirection(data, "bottom");

        cell.setLeft(left)
                .setTop(top)
                .setRight(right)
                .setBottom(bottom);

        System.out.println(cell);
    }

    private Cell cellInDirection(@NonNull final JsonNode data, @NonNull final String direction) {
        if (!data.has(direction)) return null;

        return cellWithId(data.get(direction).asText());
    }

    public Game gameWithId(@NonNull final String id) {
        return (Game) objectMap.computeIfAbsent(id, g -> new Game(id));
    }

    public Player playerWithId(@NonNull final String id) {
        return (Player) objectMap.computeIfAbsent(id, p -> new Player(id));
    }

    public Unit unitWithId(@NonNull final String id) {
        return (Unit) objectMap.computeIfAbsent(id, u -> new Unit(id));
    }

    public Cell cellWithId(@NonNull final String id) {
        return (Cell) objectMap.computeIfAbsent(id, c -> new Cell(id));
    }

    @NonNull
    private Tuple<String, String> splitIdentifier(@NonNull final String identifier) {
        final int id_index = identifier.indexOf('@');

        final String type = identifier.substring(0, id_index);

        final String id = identifier.substring(id_index + 1);

        return new Tuple<>(type, id);
    }
}
