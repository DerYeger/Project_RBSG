package de.uniks.se19.team_g.project_rbsg.ingame.battlefield.history;

import de.uniks.se19.team_g.project_rbsg.ingame.state.HistoryEntry;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.paint.Color;

public class HistoryEntryCell extends ListCell<HistoryEntry> {

    private final ActionRenderer actionRenderer;
    private Border renderedBorder = null;

    public HistoryEntryCell(ActionRenderer actionRenderer) {

        this.actionRenderer = actionRenderer;

        setText(null);
    }

    @Override
    protected void updateItem(HistoryEntry item, boolean empty) {


        if (empty) {
            renderedBorder = null;
            setGraphic(null);
            setBorder(null);
            super.updateItem(item, true);
            return;
        }

        if (getItem() == item) {
            return;
        }
        super.updateItem(item, false);


        HistoryRenderData renderData = actionRenderer.render(item.getAction());
        setGraphic(renderData.getGraphic());

        Color borderColor = renderData.getAssociatedColor();

        if (borderColor != null) {
            renderedBorder = new Border(new BorderStroke(borderColor, BorderStrokeStyle.SOLID, null, new BorderWidths(0, 0, 0, 10))
            );
        } else {
            renderedBorder = null;
        }
        setBorder(renderedBorder);
    }

    @Override
    public void updateSelected(boolean selected) {
        super.updateSelected(selected);

        if (selected) {
            setBorder(null);
        } else {
            setBorder(renderedBorder);
        }
    }
}
