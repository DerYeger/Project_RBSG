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

    @NonNull
    public Scene getLobbyScene() throws Exception {
        final VBox parent = new VBox(lobbyViewBuilder.buildLobbyScene());
        return new Scene(parent);
    }


}
