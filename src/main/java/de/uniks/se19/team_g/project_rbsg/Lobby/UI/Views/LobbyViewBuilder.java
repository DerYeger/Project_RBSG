package de.uniks.se19.team_g.project_rbsg.Lobby.UI.Views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * @author Georg Siebert
 */
@Component
public class LobbyViewBuilder
{
    private Node lobbyView;

    public @NonNull Node buildLobbyScene() throws Exception{
        if(lobbyView == null) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("LobbyView.fxml"));
            lobbyView = fxmlLoader.load();

//            final LobbyViewController lobbyViewController = fxmlLoader.getController();
//            lobbyViewController.init();
        }
        return lobbyView;
    }
}
