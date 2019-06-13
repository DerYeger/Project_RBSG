package de.uniks.se19.team_g.project_rbsg.lobby.core.ui;

import de.uniks.se19.team_g.project_rbsg.MusicManager;
import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.chat.ChatClient;
import de.uniks.se19.team_g.project_rbsg.chat.LobbyChatClient;
import de.uniks.se19.team_g.project_rbsg.chat.command.ChatCommandManager;
import de.uniks.se19.team_g.project_rbsg.chat.ui.ChatTabManager;
import de.uniks.se19.team_g.project_rbsg.configuration.FXMLLoaderFactory;
import de.uniks.se19.team_g.project_rbsg.lobby.game.CreateGameController;
import de.uniks.se19.team_g.project_rbsg.lobby.game.GameManager;
import de.uniks.se19.team_g.project_rbsg.lobby.core.PlayerManager;
import de.uniks.se19.team_g.project_rbsg.chat.ChatController;
import de.uniks.se19.team_g.project_rbsg.chat.ui.ChatBuilder;
import de.uniks.se19.team_g.project_rbsg.lobby.game.CreateGameFormBuilder;
import de.uniks.se19.team_g.project_rbsg.lobby.model.Lobby;
import de.uniks.se19.team_g.project_rbsg.lobby.model.Player;
import de.uniks.se19.team_g.project_rbsg.lobby.system.SystemMessageManager;
import de.uniks.se19.team_g.project_rbsg.model.Game;
import de.uniks.se19.team_g.project_rbsg.model.GameProvider;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.server.rest.DefaultLogoutManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.JoinGameManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.LogoutManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.RESTClient;
import de.uniks.se19.team_g.project_rbsg.server.websocket.WebSocketClient;
import io.rincl.*;
import io.rincl.resourcebundle.*;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Georg Siebert
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        FXMLLoaderFactory.class,
        PlayerListTest.ContextConfiguration.class,
        ChatBuilder.class,
        GameProvider.class,
        UserProvider.class,
        SceneManager.class,
        JoinGameManager.class,
        CreateGameFormBuilder.class,
        CreateGameController.class,
        LobbyViewBuilder.class,
        LobbyViewController.class,
        MusicManager.class
})
public class PlayerListTest extends ApplicationTest
{

    public static final Player PLAYER_1 = new Player("Hello");
    public static final Player PLAYER_2 = new Player("MOBAHero42");
    @Autowired
    ApplicationContext context;

    private LobbyViewController lobbyViewController;

    @Override
    public void start(final Stage stage) {
        Rincl.setDefaultResourceI18nConcern(new ResourceBundleResourceI18nConcern());
        LobbyViewBuilder lobbyViewBuilder = context.getBean(LobbyViewBuilder.class);
        final Scene scene = new Scene((Parent) lobbyViewBuilder.buildLobbyScene(),1200 ,840);

        stage.setScene(scene);
        stage.show();
        stage.toFront();

        lobbyViewController = lobbyViewBuilder.getLobbyViewController();
    }

    @TestConfiguration
    static class ContextConfiguration implements ApplicationContextAware {

        private ApplicationContext context;

        @Bean
        @Scope("prototype")
        public FXMLLoader fxmlLoader()
        {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setControllerFactory(this.context::getBean);
            return fxmlLoader;
        }

        @Bean
        public LogoutManager logoutManager() {
            return new DefaultLogoutManager(new RESTClient(new RestTemplate()));
        }

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
                    players.add(PLAYER_1);
                    players.add(PLAYER_2);
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
            return  new ChatController(new UserProvider(), new ChatCommandManager(), new ChatTabManager()) {
                @Override
                public void init(@NonNull final TabPane chatPane, @NonNull final ChatClient chatClient)
                {
                }
            };
        }

        @Bean
        public LobbyChatClient lobbyChatClient() {
            return new LobbyChatClient(new WebSocketClient(), new UserProvider()) {
                @Override
                public void startChatClient(@NonNull final ChatController chatController) {
                }
            };
        }

        @Override
        public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
            this.context = applicationContext;
        }
    }

    @Test
    public void addItemsAndRemove() {
        ListView<Player> playersListView = lookup("#lobbyPlayerListView").queryListView();
        assertNotNull(playersListView);

        ObservableList<Player> players = playersListView.getItems();
        assertNotNull(players);
        assertEquals(2, players.size());

        ListCell<Player> cellHello = lookup("#lobbyPlayerListView .list-cell").nth(0).query();
        ListCell<Player> cellMobaHero = lookup("#lobbyPlayerListView .list-cell").nth(1).query();

        assertNotNull(cellHello);
        assertNotNull(cellMobaHero);

        assertEquals(PLAYER_1, cellHello.getItem());
        assertEquals(PLAYER_2, cellMobaHero.getItem());


        Lobby lobby = lobbyViewController.getLobby();

        final Player carlie = new Player("Carlie");
        lobby.addPlayer(carlie);
        WaitForAsyncUtils.waitForFxEvents();

        assertEquals(3, players.size());

        ListCell<Player> cellCarlie = lookup("#lobbyPlayerListView .list-cell").nth(2).query();
        assertEquals(carlie, cellCarlie.getItem());

        Platform.runLater(() -> lobby.getPlayers().remove(0));
        WaitForAsyncUtils.waitForFxEvents();

        assertEquals(2, players.size());
    }

}
