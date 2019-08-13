package de.uniks.se19.team_g.project_rbsg.ingame.battlefield.history;

import de.uniks.se19.team_g.project_rbsg.ingame.state.Action;
import javafx.scene.Node;

public interface ActionRenderer {
    Node render(Action action);
}
