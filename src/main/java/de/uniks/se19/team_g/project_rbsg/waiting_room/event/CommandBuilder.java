package de.uniks.se19.team_g.project_rbsg.waiting_room.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

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
}
