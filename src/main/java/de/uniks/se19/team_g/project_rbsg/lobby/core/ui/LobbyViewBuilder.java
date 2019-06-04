package de.uniks.se19.team_g.project_rbsg.lobby.core.ui;

import de.uniks.se19.team_g.project_rbsg.configuration.FXMLLoaderFactory;
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
    private LobbyViewController lobbyViewController;
    private FXMLLoaderFactory loaderFactory;

    public LobbyViewController getLobbyViewController() {
        return lobbyViewController;
    }

    public LobbyViewBuilder(FXMLLoaderFactory loaderFactory) {
        this.loaderFactory = loaderFactory;
    }

    private FXMLLoader getLoader() {
        FXMLLoader loader = loaderFactory.createLoader();
        loader.setLocation(getClass().getResource("LobbyView.fxml"));

        return loader;
    }

    public @NonNull Node buildLobbyScene() {
        FXMLLoader fxmlLoader = getLoader();
        Node lobbyView;
        try
            {
                lobbyView = fxmlLoader.load();
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }

            lobbyViewController = fxmlLoader.getController();
            lobbyViewController.init();

        return lobbyView;
    }
}
