package de.uniks.se19.team_g.project_rbsg.ingame.battlefield.history;

import javafx.scene.Node;
import javafx.scene.paint.Color;

public class HistoryRenderData {

    private final Node graphic;
    private final Color associatedColor;

    public HistoryRenderData(Node graphic, Color associatedColor) {
        this.graphic = graphic;
        this.associatedColor = associatedColor;
    }

    public Node getGraphic() {
        return graphic;
    }

    public Color getAssociatedColor() {
        return associatedColor;
    }
}
