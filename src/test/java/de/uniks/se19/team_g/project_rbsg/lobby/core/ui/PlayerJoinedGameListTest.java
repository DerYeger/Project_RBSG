package de.uniks.se19.team_g.project_rbsg.lobby.core.ui;

import de.uniks.se19.team_g.project_rbsg.*;
import de.uniks.se19.team_g.project_rbsg.alert.AlertBuilder;
import de.uniks.se19.team_g.project_rbsg.chat.command.ChatCommandManager;
import de.uniks.se19.team_g.project_rbsg.configuration.*;
import de.uniks.se19.team_g.project_rbsg.chat.*;
import de.uniks.se19.team_g.project_rbsg.chat.ui.*;
import de.uniks.se19.team_g.project_rbsg.lobby.chat.LobbyChatClient;
import de.uniks.se19.team_g.project_rbsg.lobby.core.*;
import de.uniks.se19.team_g.project_rbsg.lobby.credits.CreditsFormBuilder;
import de.uniks.se19.team_g.project_rbsg.lobby.game.*;
import de.uniks.se19.team_g.project_rbsg.lobby.model.*;
import de.uniks.se19.team_g.project_rbsg.lobby.system.*;
import de.uniks.se19.team_g.project_rbsg.model.*;
import de.uniks.se19.team_g.project_rbsg.server.rest.*;
import de.uniks.se19.team_g.project_rbsg.server.websocket.*;
import io.rincl.*;
import io.rincl.resourcebundle.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;
import org.junit.*;
import org.junit.runner.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.context.*;
import org.springframework.context.annotation.*;
import org.springframework.lang.*;
import org.springframework.test.context.*;
import org.springframework.test.context.junit4.*;
import org.springframework.web.client.*;
import org.testfx.framework.junit.*;
import org.testfx.util.WaitForAsyncUtils;

import java.util.*;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        PlayerJoinedGameListTest.ContextConfiguration.class,
        ChatBuilder.class,
        GameProvider.class,
        UserProvider.class,
        SceneManager.class,
        JoinGameManager.class,
        LobbyViewController.class,
        FXMLLoaderFactory.class,
        MusicManager.class,
        ApplicationState.class,
        SceneManagerConfig.class,
        LocaleConfig.class,
        GameListViewCell.class,
        AlertBuilder.class
    }
)
public class PlayerJoinedGameListTest extends ApplicationTest
{
    @Autowired
    private ApplicationContext context;

    private Lobby lobby;

    @TestConfiguration
    public static class ContextConfiguration {
        @Bean
        public LogoutManager logoutManager() {
            return new DefaultLogoutManager(new RESTClient(new RestTemplate()));
        }

        @Bean
        public CreateGameFormBuilder createGameController()
        {
            return Mockito.mock(CreateGameFormBuilder.class);
        }

        @Bean
        public CreditsFormBuilder creditsFormBuilder() {
            return Mockito.mock(CreditsFormBuilder.class);
        }

        @Bean
        public GameManager gameManager()
        {
            return new GameManager(new RESTClient(new RestTemplate()), new UserProvider())
            {
                @Override
                public Collection<Game> getGames()
                {
                    ArrayList<Game> games = new ArrayList<>();
                    games.add(new Game("1", "game1", 2, 0));
                    return games;
                }
            };
        }

        @Bean
        public PlayerManager playerManager()
        {
            return new PlayerManager(new RESTClient(new RestTemplate()), new UserProvider())
            {
                @Override
                public Collection<Player> getPlayers()
                {
                    return new ArrayList<Player>();
                }
            };
        }

        @Bean
        public SystemMessageManager systemMessageManager()
        {
            return new SystemMessageManager(new WebSocketClient())
            {
                @Override
                public void startSocket()
                {
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
    }

    @Override
    public void start(Stage stage) {
        Rincl.setDefaultResourceI18nConcern(new ResourceBundleResourceI18nConcern());
        @SuppressWarnings("unchecked")
        ViewComponent<LobbyViewController> components = (ViewComponent<LobbyViewController>) context.getBean("lobbyScene");

        Parent parent = components.getRoot();
        Scene scene = new Scene(parent, 1200, 840);
        stage.setScene(scene);
        stage.show();
        stage.toFront();

        lobby = components.getController().getLobby();
    }

    @Test
    public void TestGameList()
    {
        Label playersLabel = lookup("#gameCellPlayersLabelgame1").query();
        assertEquals("0/2", playersLabel.textProperty().get());

        assertNotNull(lobby);
        Game gameOne = lobby.getGameOverId("1");

        assertNotNull(gameOne);

        gameOne.setJoinedPlayer(1);
        WaitForAsyncUtils.waitForFxEvents();

        assertThat(playersLabel.textProperty().get(), is("1/2") );

        gameOne.setJoinedPlayer(2);
        WaitForAsyncUtils.waitForFxEvents();

        assertThat(playersLabel.textProperty().get(), is("2/2"));

        Button playersButton = lookup("#joinGameButtongame1").queryButton();

        assertThat(playersButton, notNullValue());
        assertThat(playersButton.isDisable(), is(true));

    }
}
