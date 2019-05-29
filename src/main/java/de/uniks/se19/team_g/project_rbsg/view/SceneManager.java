package de.uniks.se19.team_g.project_rbsg.view;

import de.uniks.se19.team_g.project_rbsg.termination.RootController;
import de.uniks.se19.team_g.project_rbsg.termination.Terminable;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SceneManager {

    private Stage stage;

    private RootController rootController;

    @Autowired
    private LoginSceneBuilder loginSceneBuilder;

    @Autowired
    private LobbySceneBuilder lobbySceneBuilder;

    public SceneManager init(@NonNull final Stage stage) {
        this.stage = stage;
        return this;
    }

    public void setLoginScene() {
        if (stage == null) {
            System.out.println("Not yet initialised");
            return;
        }
        terminateCurrentRootController();
        try {
            final Scene loginScene = loginSceneBuilder.getLoginScene();
            stage.setScene(loginScene);
        } catch (IOException e) {
            System.out.println("Unable to set login scene");
            e.printStackTrace();
        }
    }

    public void setLobbyScene() {
        if (stage == null) {
            System.out.println("Not yet initialised");
            return;
        }
        terminateCurrentRootController();
        try {
            final Scene lobbyScene = lobbySceneBuilder.getLobbyScene();
            stage.setScene(lobbyScene);
        } catch (Exception e) {
            System.out.println("Unable to set login scene");
            e.printStackTrace();
        }
    }

    public void terminateCurrentRootController() {
        if (rootController != null && rootController instanceof Terminable) {
            ((Terminable) rootController).terminate();
        }
    }

    public void setRootController(@NonNull final RootController rootController) {
        this.rootController = rootController;
    }
}
