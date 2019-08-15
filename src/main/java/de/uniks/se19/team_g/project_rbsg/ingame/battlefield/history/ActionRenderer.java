package de.uniks.se19.team_g.project_rbsg.ingame.battlefield.history;

import de.uniks.se19.team_g.project_rbsg.ingame.state.Action;

public interface ActionRenderer {

    HistoryRenderData render(Action action);

    boolean supports(Action action);
}
