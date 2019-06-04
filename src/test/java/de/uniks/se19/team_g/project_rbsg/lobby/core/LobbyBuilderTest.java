package de.uniks.se19.team_g.project_rbsg.lobby.core;

import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.configuration.JavaConfig;
import de.uniks.se19.team_g.project_rbsg.ingame.IngameSceneBuilder;
import de.uniks.se19.team_g.project_rbsg.ingame.IngameViewBuilder;
import de.uniks.se19.team_g.project_rbsg.ingame.IngameViewController;
import de.uniks.se19.team_g.project_rbsg.lobby.chat.ChatController;
import de.uniks.se19.team_g.project_rbsg.lobby.chat.ChatWebSocketCallback;
import de.uniks.se19.team_g.project_rbsg.lobby.chat.ui.ChatBuilder;
import de.uniks.se19.team_g.project_rbsg.lobby.core.ui.LobbyViewBuilder;
import de.uniks.se19.team_g.project_rbsg.lobby.core.ui.LobbyViewController;
import de.uniks.se19.team_g.project_rbsg.lobby.game.CreateGameFormBuilder;
import de.uniks.se19.team_g.project_rbsg.lobby.game.GameManager;
import de.uniks.se19.team_g.project_rbsg.lobby.system.SystemMessageManager;
import de.uniks.se19.team_g.project_rbsg.login.*;
import de.uniks.se19.team_g.project_rbsg.model.GameProvider;
import de.uniks.se19.team_g.project_rbsg.model.User;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.server.rest.JoinGameManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.LoginManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.RESTClient;
import de.uniks.se19.team_g.project_rbsg.server.rest.RegistrationManager;
import de.uniks.se19.team_g.project_rbsg.server.websocket.WebSocketClient;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import org.junit.Test;
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

import static org.junit.Assert.assertNotNull;

/**
 * @author Georg Siebert
 */


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        JavaConfig.class,
        LobbyViewBuilder.class,
        LobbySceneBuilder.class,
        LobbyBuilderTest.ContextConfiguration.class,
        ChatBuilder.class,
        GameProvider.class,
        UserProvider.class,
        SceneManager.class,
        JoinGameManager.class,
        PlayerManager.class,
        GameManager.class,
        IngameViewController.class,
        IngameViewBuilder.class,
        IngameSceneBuilder.class,
        LoginFormController.class,
        LoginFormBuilder.class,
        LoginManager.class,
        RegistrationManager.class,
        SplashImageBuilder.class,
        LoginSceneBuilder.class,
        RESTClient.class,
        TitleFormBuilder.class,
        TitleFormController.class
})
public class LobbyBuilderTest extends ApplicationTest
{

    @Autowired
    private ApplicationContext context;

    private Node lobbyView;

    @Override
    public void start(@NonNull final Stage stage)
    {
        LobbyViewBuilder lobbyViewBuilder = context.getBean(LobbyViewBuilder.class);
        lobbyView = lobbyViewBuilder.buildLobbyScene();

        final Scene scene = new Scene((Parent) lobbyView);
        stage.setScene(scene);
        stage.show();
        stage.toFront();
    }

    @Test
    public void testGetLobbyView()
    {
        assertNotNull(lobbyView);
        assertNotNull(lookup("#lobbyTitle").query());
        assertNotNull(lookup("#chatContainer").query());
        assertNotNull(lookup("#lobbyGamesListView").query());
        assertNotNull(lookup("#lobbyPlayerListView").query());

    }

    @TestConfiguration
    static class ContextConfiguration
    {
        @Autowired
        private ApplicationContext context;

        @Bean
        public LobbyViewController lobbyViewController()
        {
            return new LobbyViewController(
                    context.getBean(GameProvider.class),
                    context.getBean(UserProvider.class),
                    context.getBean(SceneManager.class),
                    context.getBean(JoinGameManager.class),
                    new PlayerManager(new RESTClient(new RestTemplate()), userProvider()),
                    new GameManager(new RESTClient(new RestTemplate()), userProvider()),
                    new SystemMessageManager(new WebSocketClient()),
                    chatController(),
                    new CreateGameFormBuilder(new FXMLLoader()))
            {
                @Override
                public void init()
                {

                }
            };
        }

        @Bean
        public UserProvider userProvider()
        {
            return new UserProvider()
                    {
                        @Override
                        public User get()
                        {
                            return null;
                        }
                    };
        }

        @Bean
        public ChatController chatController()
        {
            return new ChatController(new UserProvider(), new WebSocketClient(), new ChatWebSocketCallback())
            {
                public void init(@NonNull final TabPane chatPane) throws IOException
                {
                }
            };
        }
    }

}
