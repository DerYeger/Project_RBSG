package de.uniks.se19.team_g.project_rbsg.skynet.action;

import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.Tour;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;
import org.springframework.lang.NonNull;

public class MovementAction implements Action {

    public final Unit unit;
    public final Tour tour;

    public MovementAction(@NonNull final Unit unit,
                          @NonNull final Tour tour) {
        this.unit = unit;
        this.tour = tour;
    }
}
