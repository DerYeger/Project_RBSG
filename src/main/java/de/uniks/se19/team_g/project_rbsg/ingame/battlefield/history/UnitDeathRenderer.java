package de.uniks.se19.team_g.project_rbsg.ingame.battlefield.history;

import de.uniks.se19.team_g.project_rbsg.ingame.state.Action;
import de.uniks.se19.team_g.project_rbsg.ingame.state.UnitDeathAction;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import org.springframework.stereotype.Component;

@Component
public class UnitDeathRenderer implements ActionRenderer {
    @Override
    public HistoryRenderData render(Action rawAction) {
        UnitDeathAction action = (UnitDeathAction) rawAction;

        return new HistoryRenderData(new Label(action.getUnit().getId()), Color.web(action.getPlayer().getColor()));
    }

    @Override
    public boolean supports(Action action) {
        return action instanceof UnitDeathAction;
    }
}
