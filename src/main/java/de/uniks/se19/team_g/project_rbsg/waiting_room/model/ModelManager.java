package de.uniks.se19.team_g.project_rbsg.waiting_room.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.uniks.se19.team_g.project_rbsg.waiting_room.event.GameEventHandler;
import de.uniks.se19.team_g.project_rbsg.util.Tuple;
import de.uniks.se19.team_g.project_rbsg.waiting_room.model.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import java.util.HashMap;

/**
 * @author Jan Müller
 */
public class ModelManager implements GameEventHandler {

    @NonNull
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @NonNull
    private final HashMap<String, Object> objectMap;

    private de.uniks.se19.team_g.project_rbsg.waiting_room.model.Game game;

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
                //FOR DEMONSTRATION
                logger.debug("Game has " + game.getCells().size() + " cells");
                final Player firstPlayer = game.getPlayers().get(0);
                logger.debug(firstPlayer.getName() + " has " + firstPlayer.getUnits().size() + " units");
                logger.debug(firstPlayer.getName() + "'s first unit has type " + firstPlayer.getUnits().get(0).getUnitType());
                logger.debug("It can attack " + firstPlayer.getUnits().get(0).getCanAttack());
                logger.debug("Its position is " + firstPlayer.getUnits().get(0).getPosition());
                //END
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
                game = GameUtil.buildGame(this, identifier, data, false);
                break;
            case "Player":
                PlayerUtil.buildPlayer(this, identifier, data, false);
                break;
            case "Unit":
                UnitUtil.buildUnit(this, identifier, data, false);
                break;
            case "Forest":
            case "Grass":
            case "Mountain":
            case "Water":
                CellUtil.buildCell(this, identifier, StringToEnum.biome(type), data, false);
                break;
            default:
                logger.debug("Unknown class");
        }
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
        final int at_index = identifier.indexOf('@');
        final String clazz = identifier.substring(0, at_index);
        final String code = identifier.substring(at_index + 1);
        return new Tuple<>(clazz, code);
    }
}
