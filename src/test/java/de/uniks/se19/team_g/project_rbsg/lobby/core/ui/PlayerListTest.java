package de.uniks.se19.team_g.project_rbsg.lobby.core.ui;

import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.configuration.FXMLLoaderFactory;
import de.uniks.se19.team_g.project_rbsg.configuration.JavaConfig;
import de.uniks.se19.team_g.project_rbsg.ingame.IngameSceneBuilder;
import de.uniks.se19.team_g.project_rbsg.ingame.IngameViewBuilder;
import de.uniks.se19.team_g.project_rbsg.ingame.IngameViewController;
import de.uniks.se19.team_g.project_rbsg.lobby.core.LobbySceneBuilder;
import de.uniks.se19.team_g.project_rbsg.lobby.game.GameManager;
import de.uniks.se19.team_g.project_rbsg.lobby.core.PlayerManager;
import de.uniks.se19.team_g.project_rbsg.lobby.chat.ChatController;
import de.uniks.se19.team_g.project_rbsg.lobby.chat.ChatWebSocketCallback;
import de.uniks.se19.team_g.project_rbsg.lobby.chat.ui.ChatBuilder;
import de.uniks.se19.team_g.project_rbsg.lobby.game.CreateGameFormBuilder;
import de.uniks.se19.team_g.project_rbsg.lobby.model.Lobby;
import de.uniks.se19.team_g.project_rbsg.lobby.model.Player;
import de.uniks.se19.team_g.project_rbsg.lobby.system.SystemMessageManager;
import de.uniks.se19.team_g.project_rbsg.login.*;
import de.uniks.se19.team_g.project_rbsg.model.Game;
import de.uniks.se19.team_g.project_rbsg.model.GameProvider;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.server.rest.JoinGameManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.LoginManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.RESTClient;
import de.uniks.se19.team_g.project_rbsg.server.rest.RegistrationManager;
import de.uniks.se19.team_g.project_rbsg.server.websocket.WebSocketClient;
import de.uniks.se19.team_g.project_rbsg.termination.Terminator;
import io.rincl.*;
import io.rincl.resourcebundle.*;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;
import org.testfx.framework.junit.ApplicationTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Georg Siebert
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        JavaConfig.class,
        FXMLLoaderFactory.class,
        LobbyViewBuilder.class,
        LobbyViewController.class,
        LobbySceneBuilder.class,
        PlayerListTest.ContextConfiguration.class,
        ChatBuilder.class,
        SceneManager.class,
        Terminator.class,
        GameProvider.class,
        UserProvider.class,
        SceneManager.class,
        IngameSceneBuilder.class,
        IngameViewBuilder.class,
        IngameViewController.class,
        LoginFormController.class,
        LoginFormBuilder.class,
        LoginManager.class,
        RegistrationManager.class,
        SplashImageBuilder.class,
        LoginSceneBuilder.class,
        JoinGameManager.class,
        CreateGameFormBuilder.class,
        TitleViewBuilder.class,
        TitleViewController.class
})
public class PlayerListTest extends ApplicationTest
{

    @Autowired
    ApplicationContext context;

    @Override
    public void start(final Stage stage) {
        Rincl.setDefaultResourceI18nConcern(new ResourceBundleResourceI18nConcern());
        LobbyViewBuilder lobbyViewBuilder = context.getBean(LobbyViewBuilder.class);
        final Scene scene = new Scene((Parent) lobbyViewBuilder.buildLobbyScene());

        stage.setScene(scene);
        stage.show();
        stage.toFront();
    }

    @TestConfiguration
    static class ContextConfiguration {
        @Bean
        public GameManager gameManager() {
            return new GameManager(new RESTClient(new RestTemplate()), new UserProvider()) {
                @Override
                public Collection<Game> getGames() {
                    return new ArrayList<Game>();
                }
            };
        }

        @Bean
        public PlayerManager playerManager() {
            return new PlayerManager(new RESTClient(new RestTemplate()), new UserProvider()) {
                @Override
                public Collection<Player> getPlayers() {
                    ArrayList<Player> players = new ArrayList<>();
                    players.add(new Player("Hello"));
                    players.add(new Player("MOBAHero42"));
                    return players;
                }
            };
        }

        @Bean
        public SystemMessageManager systemMessageManager() {
            return new SystemMessageManager(new WebSocketClient()) {
                @Override
                public void startSocket() {
                }
            };
        }

        @Bean
        public ChatController chatController() {
            return  new ChatController(new UserProvider(), new WebSocketClient(), new ChatWebSocketCallback()) {
                @Override
                public void init(@NonNull final TabPane chatPane) throws IOException
                {
                }
            };
        }
    }

    @Test
    public void addItemsAndRemove() {
        ListView<Player> playersListView = lookup("#lobbyPlayerListView").queryListView();
        assertNotNull(playersListView);

        ObservableList<Player> players = playersListView.getItems();
        assertNotNull(players);
        assertEquals(2, players.size());

        ListCell<Player> cellHello = lookup("#playerCellHello").query();
        ListCell<Player> cellMobaHero = lookup("#playerCellMOBAHero42").query();

//        Player hello = cellHello.getItem().;


        assertNotNull(cellHello);
        assertNotNull(cellMobaHero);

        clickOn("#playerCellHello");
        clickOn("#playerCellMOBAHero42");

        assertEquals("Hello", cellHello.getItem().getName());
        assertEquals("MOBAHero42", cellMobaHero.getItem().getName());

        rightClickOn("#playerCellHello");
        rightClickOn("#playerCellMOBAHero42");

        Lobby lobby = context.getBean(LobbyViewController.class).getLobby();

        lobby.addPlayer(new Player("Carlie"));
        sleep(500);

        assertEquals(3, players.size());

        ListCell<Player> cellCarlie = lookup("#playerCellCarlie").query();
        assertNotNull(cellCarlie);

        assertEquals("Carlie", cellCarlie.getItem().getName());

        rightClickOn("#playerCellCarlie");

        Platform.runLater(() -> lobby.getPlayers().remove(0));
        sleep(500);

        assertEquals(2, players.size());
    }

}
