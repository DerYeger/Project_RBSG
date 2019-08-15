package de.uniks.se19.team_g.project_rbsg.ingame.battlefield.history;

import de.uniks.se19.team_g.project_rbsg.ingame.state.HistoryEntry;
import javafx.scene.control.ListCell;

public class HistoryEntryCell extends ListCell<HistoryEntry> {

    private final ActionRenderer actionRenderer;

    public HistoryEntryCell(ActionRenderer actionRenderer) {

        this.actionRenderer = actionRenderer;

        setText(null);
    }

    @Override
    protected void updateItem(HistoryEntry item, boolean empty) {
        super.updateItem(item, empty);

        if (isEmpty()) {
            setGraphic(null);
            return;
        }

        setGraphic(actionRenderer.render(item.getAction()));
    }
}
