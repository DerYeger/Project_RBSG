package de.uniks.se19.team_g.project_rbsg.view;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SceneManager {

    private String applicationName = "RBSG - Advanced Wars TM";
    private Stage stage;

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
        try {
            final Scene loginScene = loginSceneBuilder.getLoginScene();
            stage.setScene(loginScene);
            stage.setTitle(applicationName);
            stage.getIcons().add(new Image(SceneManager.class.getResourceAsStream("icon.png")));
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
        try {
            final Scene lobbyScene = lobbySceneBuilder.getLobbyScene();
            stage.setScene(lobbyScene);
            stage.setTitle(applicationName);
            stage.getIcons().add(new Image(SceneManager.class.getResourceAsStream("icon.png")));
        } catch (Exception e) {
            System.out.println("Unable to set login scene");
            e.printStackTrace();
        }
    }
}
