package de.uniks.se19.team_g.project_rbsg.lobby.core.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Georg Siebert
 */
@Component
public class LobbyViewBuilder
{
    private Node lobbyView;
    private FXMLLoader fxmlLoader;
    private LobbyViewController lobbyViewController;

    public LobbyViewController getLobbyViewController() {
        return lobbyViewController;
    }

    public LobbyViewBuilder(FXMLLoader fxmlLoader) {
        this.fxmlLoader = fxmlLoader;
    }

    public @NonNull Node buildLobbyScene() {
        if(lobbyView == null) {
            fxmlLoader.setLocation(getClass().getResource("LobbyView.fxml"));
            try
            {
                lobbyView = fxmlLoader.load();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            lobbyViewController = fxmlLoader.getController();
            lobbyViewController.init();
        }
        return lobbyView;
    }
}