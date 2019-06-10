package de.uniks.se19.team_g.project_rbsg.waiting_room;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author  Keanu Stückrad
 */
@Component
public class WaitingRoomViewBuilder {

    private Node waitingRoomView;

    @Autowired
    private FXMLLoader fxmlLoader;

    public Node buildIngameView() throws Exception{
        if(waitingRoomView == null) {
            fxmlLoader.setLocation(getClass().getResource("waiting-room-view.fxml"));
            waitingRoomView = fxmlLoader.load();
            final WaitingRoomViewController waitingRoomViewController = fxmlLoader.getController();
            waitingRoomViewController.init();
        }
        return waitingRoomView;
    }
}
