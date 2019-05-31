package de.uniks.se19.team_g.project_rbsg.lobby.core;

import de.uniks.se19.team_g.project_rbsg.lobby.core.ui.LobbyViewBuilder;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class LobbySceneBuilder {

    @Autowired
    private LobbyViewBuilder lobbyViewBuilder;

    private Scene lobbyScene;

    @NonNull
    public Scene getLobbyScene() throws Exception {
        if (lobbyScene == null) {
            final VBox parent = new VBox(lobbyViewBuilder.buildLobbyScene());
            lobbyScene = new Scene(parent);
        }
        return lobbyScene;
    }


}
