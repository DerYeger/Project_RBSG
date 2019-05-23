package de.uniks.se19.team_g.project_rbsg.Lobby.UI.Views;

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

            final LobbyViewController lobbyViewController = fxmlLoader.getController();
            lobbyViewController.init();
        }
        return lobbyView;
    }
}
