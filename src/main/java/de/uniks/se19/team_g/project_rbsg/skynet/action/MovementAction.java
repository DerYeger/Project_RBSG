package de.uniks.se19.team_g.project_rbsg.skynet.action;

import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.Tour;
import de.uniks.se19.team_g.project_rbsg.ingame.event.CommandBuilder;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;
import org.springframework.lang.NonNull;

import java.util.Map;

public class MovementAction implements Action {

    public final Unit unit;
    public final Tour tour;

    public MovementAction(@NonNull final Unit unit,
                          @NonNull final Tour tour) {
        this.unit = unit;
        this.tour = tour;
    }

    @Override
    public Map<String, Object> getServerCommand() {
        return CommandBuilder.moveUnit(unit, tour.getPath());
    }
}
