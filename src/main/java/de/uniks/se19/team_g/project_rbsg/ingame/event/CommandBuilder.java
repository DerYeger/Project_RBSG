package de.uniks.se19.team_g.project_rbsg.ingame.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Cell;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;
import de.uniks.se19.team_g.project_rbsg.model.Army;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public static Map<String, Object> moveUnit(Unit unit, List<Cell> sortedList)
    {
        MoveUnitData data = new MoveUnitData();
        data.unitId = unit.getId();
        data.path = sortedList.stream().map(Cell::getId).collect(Collectors.toList());

        Map<String, Object> command = command("moveUnit");
        command.put("data", data);

        return command;
    }

    static class MoveUnitData {
        public String unitId;
        public List<String> path;
    }
}
