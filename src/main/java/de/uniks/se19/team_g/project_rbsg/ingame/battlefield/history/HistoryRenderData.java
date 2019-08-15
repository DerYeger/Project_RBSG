package de.uniks.se19.team_g.project_rbsg.ingame.battlefield.history;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import javafx.scene.Node;

public class HistoryRenderData {

    private final Node graphic;
    private final Player affectedPlayer;

    public HistoryRenderData(Node graphic, Player affectedPlayer) {
        this.graphic = graphic;
        this.affectedPlayer = affectedPlayer;
    }

    public Node getGraphic() {
        return graphic;
    }

    public Player getAffectedPlayer() {
        return affectedPlayer;
    }
}
