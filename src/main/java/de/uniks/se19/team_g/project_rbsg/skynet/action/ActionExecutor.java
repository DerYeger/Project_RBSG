package de.uniks.se19.team_g.project_rbsg.skynet.action;

import de.uniks.se19.team_g.project_rbsg.ingame.event.IngameApi;
import org.springframework.lang.NonNull;

public class ActionExecutor {

    private final IngameApi api;

    public ActionExecutor(@NonNull final IngameApi api) {
        this.api = api;
    }

    public void execute(@NonNull final MovementAction action) {
        api.move(action.unit, action.tour);
    }
}