package de.uniks.se19.team_g.project_rbsg.ingame.battlefield.history;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Cell;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;
import de.uniks.se19.team_g.project_rbsg.ingame.state.Action;
import de.uniks.se19.team_g.project_rbsg.ingame.state.ActionImpl;
import javafx.scene.Node;
import javafx.scene.control.Label;

public class MovementActionRenderer implements ActionRenderer {
    @Override
    public Node render(Action action) {
        if ( !(action instanceof ActionImpl)) {
            return null;
        }
        ActionImpl actionImpl = (ActionImpl) action;
        if ( !(actionImpl.getEntity() instanceof Unit && actionImpl.getNextValue() instanceof Cell)) {
            return null;
        }

        return new Label(
            String.format("%s: %s", ((Unit) actionImpl.getEntity()).getId(), actionImpl.getNextValue().toString())
        );
    }
}
