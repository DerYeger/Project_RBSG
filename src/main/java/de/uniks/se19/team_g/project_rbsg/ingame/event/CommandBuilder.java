package de.uniks.se19.team_g.project_rbsg.ingame.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.uniks.se19.team_g.project_rbsg.model.Army;

import java.util.HashMap;
import java.util.Map;

public class CommandBuilder {

    private static String MESSAGE_TYPE = "messageType";

    private static String COMMAND = "command";

    private static String ACTION = "action";

    private static String LEAVE_GAME = "leaveGame";

    private static String END_PHASE = "nextPhase";

    public static ObjectNode leaveGameCommand() {
        return new ObjectMapper()
                .createObjectNode()
                .put(MESSAGE_TYPE, COMMAND)
                .put(ACTION, LEAVE_GAME);
    }

    public static ObjectNode endPhaseCommand() {
        return new ObjectMapper()
                .createObjectNode()
                .put(MESSAGE_TYPE, COMMAND)
                .put(ACTION, END_PHASE);
    }

    public static Map<String,Object> readyToPlay() {
        Map<String, Object> command = new HashMap<>();
        command.put(MESSAGE_TYPE, COMMAND);
        command.put(ACTION, "readyToPlay");
        return command;
    }

    public static Map<String,Object> startGame() {
        Map<String, Object> command = new HashMap<>();
        command.put(MESSAGE_TYPE, COMMAND);
        command.put(ACTION, "startGame");
        return command;
    }

    public static Map<String, Object> changeArmy(Army army) {
        final Map<String, Object> command = command("changeArmy");
        command.put("data", army.id.get());
        return command;
    }

    public static Map<String, Object> command(String action) {
        final HashMap<String, Object> command = new HashMap<>();
        command.put(MESSAGE_TYPE, COMMAND);
        command.put(ACTION, action);
        return command;

    }
}
