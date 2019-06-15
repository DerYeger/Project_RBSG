package de.uniks.se19.team_g.project_rbsg.waiting_room;

import de.uniks.se19.team_g.project_rbsg.configuration.FXMLLoaderFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author  Keanu Stückrad
 */
@Component
public class WaitingRoomViewBuilder {

    private WaitingRoomViewController waitingRoomViewController;
    private FXMLLoaderFactory fxmlLoaderFactory;
    private Node waitingRoomView;

    public WaitingRoomViewBuilder(FXMLLoaderFactory fxmlLoaderFactory) {
        this.fxmlLoaderFactory = fxmlLoaderFactory;
    }

    private FXMLLoader getLoader() {
        FXMLLoader loader = fxmlLoaderFactory.fxmlLoader();
        loader.setLocation(getClass().getResource("/ui/waiting_room/waitingRoomView.fxml"));
        return loader;
    }

    public @NonNull Node buildIngameView() {
        FXMLLoader fxmlLoader = getLoader();
        try {
            waitingRoomView = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        waitingRoomViewController = fxmlLoader.getController();
        waitingRoomViewController.init();
        return waitingRoomView;
    }
}
