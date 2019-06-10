package de.uniks.se19.team_g.project_rbsg.waiting_room;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * @author  Keanu Stückrad
 */
@Component
public class WaitingRoomSceneBuilder {

    @Autowired
    private WaitingRoomViewBuilder waitingRoomViewBuilder;

    private Scene waitingRoomScene;

    @NonNull
    public Scene getWaitingRoomScene() throws Exception {
        if (waitingRoomScene == null) {
            Node waitingRoomNode = waitingRoomViewBuilder.buildIngameView();
            waitingRoomScene = new Scene((Parent) waitingRoomNode);
        }
        return waitingRoomScene;
    }
}
