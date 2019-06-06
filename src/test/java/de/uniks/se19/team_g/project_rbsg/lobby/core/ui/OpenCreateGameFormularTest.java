package de.uniks.se19.team_g.project_rbsg.lobby.core.ui;

import de.uniks.se19.team_g.project_rbsg.*;
import de.uniks.se19.team_g.project_rbsg.configuration.*;
import de.uniks.se19.team_g.project_rbsg.ingame.*;
import de.uniks.se19.team_g.project_rbsg.lobby.chat.*;
import de.uniks.se19.team_g.project_rbsg.lobby.chat.ui.*;
import de.uniks.se19.team_g.project_rbsg.lobby.core.*;
import de.uniks.se19.team_g.project_rbsg.lobby.game.*;
import de.uniks.se19.team_g.project_rbsg.lobby.model.*;
import de.uniks.se19.team_g.project_rbsg.lobby.system.*;
import de.uniks.se19.team_g.project_rbsg.login.*;
import de.uniks.se19.team_g.project_rbsg.model.*;
import de.uniks.se19.team_g.project_rbsg.server.rest.*;
import de.uniks.se19.team_g.project_rbsg.server.websocket.*;
import de.uniks.se19.team_g.project_rbsg.termination.Terminator;
import io.rincl.*;
import io.rincl.resourcebundle.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.context.*;
import org.springframework.context.annotation.*;
import org.springframework.lang.*;
import org.springframework.test.context.*;
import org.springframework.test.context.junit4.*;
import org.springframework.web.client.*;
import org.testfx.api.*;
import org.testfx.framework.junit.*;

import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        JavaConfig.class,
        LobbyViewBuilder.class,
        LobbyViewController.class,
        LobbySceneBuilder.class,
        OpenCreateGameFormularTest.ContextConfiguration.class,
        ChatBuilder.class,
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
        CreateGameController.class,
        TitleViewBuilder.class,
        TitleViewController.class,
        Terminator.class
})
public class OpenCreateGameFormularTest extends ApplicationTest
{

    @Autowired
    ApplicationContext context;

    @TestConfiguration
    public static class ContextConfiguration
    {
        @Bean
        public GameManager gameManager()
        {
            return new GameManager(new RESTClient(new RestTemplate()), new UserProvider())
            {
                @Override
                public Collection<Game> getGames()
                {
                   return new ArrayList<>();
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
        public ChatController chatController()
        {
            return new ChatController(new UserProvider(), new WebSocketClient(), new ChatWebSocketCallback())
            {
                @Override
                public void init(@NonNull final TabPane chatPane)
                {
                }

            };
        }
    }

    @Override
    public void start(Stage stage) {
        Rincl.setDefaultResourceI18nConcern(new ResourceBundleResourceI18nConcern());
        LobbyViewBuilder lobbyViewBuilder = context.getBean(LobbyViewBuilder.class);

        final Scene scene = new Scene((Parent) lobbyViewBuilder.buildLobbyScene());

        stage.setScene(scene);
        stage.show();
        stage.toFront();
    }


    @Override
    public void stop() throws Exception
    {
        FxToolkit.hideStage();
    }

    @Test
    public void openCreateGame() {
        clickOn("#createGameButton");
        StackPane stackPane = lookup("#mainStackPane").query();

        assertEquals(2, stackPane.getChildren().size());
    }

}
