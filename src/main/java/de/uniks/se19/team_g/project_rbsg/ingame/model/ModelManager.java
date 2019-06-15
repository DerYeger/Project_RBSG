package de.uniks.se19.team_g.project_rbsg.ingame.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.uniks.se19.team_g.project_rbsg.ingame.event.GameEventHandler;
import de.uniks.se19.team_g.project_rbsg.util.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import java.util.HashMap;
import java.util.HashSet;

public class ModelManager implements GameEventHandler {

    @NonNull
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @NonNull
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
            case "gameInitFinished":
                logger.debug("Game has " + game.getCells().size() + " cells");
                logger.debug("Player has " + game.getPlayers().iterator().next().getUnits().size() + " units");
                break;
            default:
                logger.debug("Not a model message");
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
            case "Grass":
            case "Mountain":
            case "Water":
                initCell(identifier, Biome.valueOf(type.toUpperCase()), data);
                break;
            default:
                logger.debug("Unknown class");
        }
    }

    private void initGame(@NonNull final String identifier, @NonNull final JsonNode data) {
        game = gameWithId(identifier);

        if (data.has("allPlayer")) {
            final JsonNode players = data.get("allPlayer");
            if (players.isArray()) {
                for (final JsonNode player : players) {
                    game.withPlayer(playerWithId(player.asText()));
                }
            }
        }

        if (data.has("allUnits")) {
            final JsonNode units = data.get("allUnits");
            if (units.isArray()) {
                for (final JsonNode unit : units) {
                    game.withUnit(unitWithId(unit.asText()));
                }
            }
        }

        logger.debug("Added game: " + game);
    }

    private void initPlayer(@NonNull final String identifier, @NonNull final JsonNode data) {
        final Player player = playerWithId(identifier);

        if (data.has("currentGame")) player.setGame(gameWithId(data.get("currentGame").asText()));
        if (data.has("name")) player.setName(data.get("name").asText());
        if (data.has("color")) player.setColor(data.get("color").asText());

        if (data.has("army")) {
            final JsonNode army = data.get("army");
            if (army.isArray()) {
                for (final JsonNode unit : army) {
                    player.withUnit(unitWithId(unit.asText()));
                }
            }
        }

//        logger.debug("Added player: " + player);
    }

    private void initUnit(@NonNull final String identifier, @NonNull final JsonNode data) {
        final Unit unit = unitWithId(identifier);

        if (data.has("type")) unit.setUnitType(UnitType.valueOf(data.get("type").asText().toUpperCase().replace(" ", "_")));
        if (data.has("mp")) unit.setMp(data.get("mp").asInt());
        if (data.has("hp")) unit.setHp(data.get("hp").asInt());
        if (data.has("game")) unit.setGame(gameWithId(data.get("game").asText()));
        if (data.has("leader")) unit.setLeader(playerWithId(data.get("leader").asText()));
        if (data.has("position")) unit.doSetPosition(cellWithId(data.get("position").asText()));

        if (data.has("canAttack")) {
            final JsonNode unitTypes = data.get("canAttack");
            if (unitTypes.isArray()) {
                final HashSet<UnitType> canAttack = new HashSet<>();
                for (final JsonNode unitType : unitTypes) {
                    canAttack.add(UnitType.valueOf(unitType.asText().toUpperCase().replace(" ", "_")));
                }
                unit.setCanAttack(canAttack);
            }
        }

//        logger.debug("Added unit: " + unit);
    }

    private void initCell(@NonNull final String identifier, @NonNull final Biome biome, @NonNull final JsonNode data) {
        final Cell cell = cellWithId(identifier).setBiome(biome);

        if (data.has("game")) cell.setGame(gameWithId(data.get("game").asText()));
        if (data.has("isPassable")) cell.setPassable(data.get("isPassable").asBoolean());
        if (data.has("x")) cell.setX(data.get("x").asInt());
        if (data.has("y")) cell.setY(data.get("y").asInt());

        final Cell left = cellInDirection(data, "left");
        final Cell top = cellInDirection(data, "top");
        final Cell right = cellInDirection(data, "right");
        final Cell bottom = cellInDirection(data, "bottom");

        cell.setLeft(left)
                .setTop(top)
                .setRight(right)
                .setBottom(bottom);

//        logger.debug("Added cell:" + cell);
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
