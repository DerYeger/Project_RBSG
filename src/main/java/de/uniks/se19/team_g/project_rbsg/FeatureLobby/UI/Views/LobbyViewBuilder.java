package de.uniks.se19.team_g.project_rbsg.FeatureLobby.UI.Views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import org.springframework.lang.NonNull;

/**
 * @author Georg Siebert
 */

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
