package de.uniks.se19.team_g.project_rbsg.skynet.action;

import de.uniks.se19.team_g.project_rbsg.ingame.event.CommandBuilder;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Cell;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Map;

public class MovementAction implements Action {

    private final Unit unit;
    private final List<Cell> path;

    public MovementAction(@NonNull final Unit unit,
                          @NonNull final List<Cell> path) {
        this.unit = unit;
        this.path = path;
    }

    @Override
    public Map<String, Object> getServerCommand() {
        return CommandBuilder.moveUnit(unit, path);
    }
}
