package de.uniks.se19.team_g.project_rbsg.ingame.battlefield.history;

import de.uniks.se19.team_g.project_rbsg.ingame.state.HistoryEntry;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.function.BiConsumer;

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

        Node root = actionRenderer.render(item.getAction());
        setGraphic(root);
    }
}
