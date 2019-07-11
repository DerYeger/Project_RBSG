package de.uniks.se19.team_g.project_rbsg.ingame;

import de.uniks.se19.team_g.project_rbsg.RootController;
import de.uniks.se19.team_g.project_rbsg.ViewComponent;
import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.IngameViewController;
import de.uniks.se19.team_g.project_rbsg.ingame.waiting_room.WaitingRoomViewController;
import de.uniks.se19.team_g.project_rbsg.termination.Terminable;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.net.URL;
import java.util.ResourceBundle;

@Component
public class IngameRootController
    implements RootController, Initializable, Terminable
{

    public StackPane root;

    @Nonnull
    private final ObjectFactory<ViewComponent<WaitingRoomViewController>> waitingRoomFactory;

    @Nonnull
    private final ObjectFactory<ViewComponent<IngameViewController>> ingameFactory;

    private ViewComponent activeComponent;

    public IngameRootController(
        @Nonnull ObjectFactory<ViewComponent<WaitingRoomViewController>> waitingRoomFactory,
        @Nonnull ObjectFactory<ViewComponent<IngameViewController>> ingameFactory
    ) {
        this.waitingRoomFactory = waitingRoomFactory;
        this.ingameFactory = ingameFactory;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        activeComponent = waitingRoomFactory.getObject();
        final Node root = activeComponent.getRoot();
        this.root.getChildren().add(root);
    }

    @Override
    public void terminate() {
        if (activeComponent.getController() instanceof Terminable) {
            ((Terminable) activeComponent.getController()).terminate();
        }
    }
}
