package de.uniks.se19.team_g.project_rbsg.waiting_room.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.HashMap;
import java.util.Map;

public class CommandBuilder {

    private static String MESSAGE_TYPE = "messageType";

    private static String COMMAND = "command";

    private static String ACTION = "action";

    private static String LEAVE_GAME = "leaveGame";

    public static ObjectNode leaveGameCommand() {
        return new ObjectMapper()
                .createObjectNode()
                .put(MESSAGE_TYPE, COMMAND)
                .put(ACTION, LEAVE_GAME);
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
}
