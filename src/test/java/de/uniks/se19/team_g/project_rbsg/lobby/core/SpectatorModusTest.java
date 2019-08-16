package de.uniks.se19.team_g.project_rbsg.lobby.core;

import de.uniks.se19.team_g.project_rbsg.MusicManager;
import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.ViewComponent;
import de.uniks.se19.team_g.project_rbsg.alert.AlertBuilder;
import de.uniks.se19.team_g.project_rbsg.chat.ChatClient;
import de.uniks.se19.team_g.project_rbsg.chat.ChatController;
import de.uniks.se19.team_g.project_rbsg.chat.command.ChatCommandManager;
import de.uniks.se19.team_g.project_rbsg.chat.ui.ChatBuilder;
import de.uniks.se19.team_g.project_rbsg.chat.ui.ChatTabManager;
import de.uniks.se19.team_g.project_rbsg.configuration.ApplicationState;
import de.uniks.se19.team_g.project_rbsg.configuration.FXMLLoaderFactory;
import de.uniks.se19.team_g.project_rbsg.configuration.LocaleConfig;
import de.uniks.se19.team_g.project_rbsg.configuration.SceneManagerConfig;
import de.uniks.se19.team_g.project_rbsg.ingame.IngameContext;
import de.uniks.se19.team_g.project_rbsg.ingame.IngameRootController;
import de.uniks.se19.team_g.project_rbsg.ingame.event.GameEventManager;
import de.uniks.se19.team_g.project_rbsg.lobby.chat.LobbyChatClient;
import de.uniks.se19.team_g.project_rbsg.lobby.core.ui.GameListViewCell;
import de.uniks.se19.team_g.project_rbsg.lobby.core.ui.LobbyViewController;
import de.uniks.se19.team_g.project_rbsg.lobby.credits.CreditsFormBuilder;
import de.uniks.se19.team_g.project_rbsg.lobby.game.CreateGameController;
import de.uniks.se19.team_g.project_rbsg.lobby.game.CreateGameFormBuilder;
import de.uniks.se19.team_g.project_rbsg.lobby.game.GameManager;
import de.uniks.se19.team_g.project_rbsg.lobby.model.Player;
import de.uniks.se19.team_g.project_rbsg.lobby.system.SystemMessageManager;
import de.uniks.se19.team_g.project_rbsg.model.Game;
import de.uniks.se19.team_g.project_rbsg.model.GameProvider;
import de.uniks.se19.team_g.project_rbsg.model.IngameGameProvider;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.server.rest.DefaultLogoutManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.JoinGameManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.LogoutManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.RESTClient;
import de.uniks.se19.team_g.project_rbsg.server.websocket.IWebSocketCallback;
import de.uniks.se19.team_g.project_rbsg.server.websocket.WebSocketClient;
import io.rincl.Rincl;
import io.rincl.resourcebundle.ResourceBundleResourceI18nConcern;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        FXMLLoaderFactory.class,
        ChatBuilder.class,
        GameProvider.class,
        UserProvider.class,
        SceneManager.class,
        GameListViewCell.class,
        CreateGameFormBuilder.class,
        CreditsFormBuilder.class,
        LobbyViewController.class,
        MusicManager.class,
        ApplicationState.class,
        SceneManagerConfig.class,
        AlertBuilder.class,
        LocaleConfig.class,
        IngameContext.class,
        IngameGameProvider.class,
        IngameRootController.class,
        JoinGameManager.class,
        SpectatorModusTest.ContextConfiguration.class,
        EmailManager.class
})
public class SpectatorModusTest extends ApplicationTest {

    private static final Game NEW_GAME = new Game("1", "myNewGame", 2, 1);

    @Autowired
    GameProvider gameProvider;

    @Autowired
    UserProvider userProvider;

    @Autowired
    GameEventManager gameEventManager;

    private LobbyViewController lobbyViewController;

    @TestConfiguration
    public static class ContextConfiguration {

        @Bean
        public CreateGameController createGameController()
        {
            return Mockito.mock(CreateGameController.class);
        }

        @Bean
        public CreditsFormBuilder creditsFormBuilder()
        {
            return Mockito.mock(CreditsFormBuilder.class);
        }

        @Bean
        public LogoutManager logoutManager() {
            return new DefaultLogoutManager(new RESTClient(new RestTemplate()));
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
                    games.add(NEW_GAME);
                    return games;
                }
            };
        }

        @Bean
        public PlayerManager playerManager() {
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
                public void init(@NonNull final TabPane chatPane, @NonNull final ChatClient chatClient) {
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

        @Bean
        public GameEventManager gameEventManager() {
            return new GameEventManager(new WebSocketClient(){
                @Override
                public void start(final @NotNull String endpoint, final @NotNull IWebSocketCallback wsCallback) throws Exception {
                    String uriEndpoint = "/game?gameId=1&spectator=true";

                    Assert.assertEquals(uriEndpoint, endpoint);
                }
            });
        }

    }

    @Autowired
    private ApplicationContext context;

    @Autowired
    IngameContext ingameContext;

    @Override
    public void start(Stage stage) {

        ingameContext.getUser().setUserKey("123");
        ingameContext.getUser().setName("Bob");
        Rincl.setDefaultResourceI18nConcern(new ResourceBundleResourceI18nConcern());
        @SuppressWarnings("unchecked")
        ViewComponent<LobbyViewController> components = (ViewComponent<LobbyViewController>) context.getBean("lobbyScene");

        final Scene scene = new Scene(components.getRoot(),1200 ,840);

        stage.setScene(scene);
        stage.show();
        stage.toFront();

        lobbyViewController = components.getController();
    }

    @Test
    public void joinAsSpectatorTest() throws Exception {

        ListCell<Game> newGame = lookup("#lobbyGamesListView .list-cell").nth(0).query();
        Button spectatorButton = from(newGame).lookup("#spectatorButtonmyNewGame").query();
        clickOn(spectatorButton);

        WaitForAsyncUtils.waitForFxEvents();

        Assert.assertTrue(NEW_GAME.isSpectatorModus());

        gameEventManager.startSocket("1", null, true);
    }
}


