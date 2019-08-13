package de.uniks.se19.team_g.project_rbsg.ingame.battlefield.history;

import de.uniks.se19.team_g.project_rbsg.ingame.IngameContext;
import de.uniks.se19.team_g.project_rbsg.ingame.state.HistoryEntry;
import javafx.scene.control.ListView;
import org.springframework.stereotype.Component;

@Component
public class HistoryViewController {

    public ListView<HistoryEntry> history;

    final private ActionRenderer actionRenderer;

    private IngameContext context;

    public HistoryViewController(ActionRenderer actionRenderer) {
        this.actionRenderer = actionRenderer;
    }

    public void configureContext(IngameContext context) {
        this.context = context;

        configureHistory();
    }

    private void configureHistory() {
        history.setCellFactory( param -> new ActionCell(actionRenderer));

        history.setItems(context.getModelManager().getHistory().getEntries());
    }
}
