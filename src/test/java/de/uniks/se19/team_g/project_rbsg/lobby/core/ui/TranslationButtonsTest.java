package de.uniks.se19.team_g.project_rbsg.lobby.core.ui;

import de.uniks.se19.team_g.project_rbsg.*;
import de.uniks.se19.team_g.project_rbsg.alert.AlertBuilder;
import de.uniks.se19.team_g.project_rbsg.chat.command.ChatCommandManager;
import de.uniks.se19.team_g.project_rbsg.configuration.ApplicationState;
import de.uniks.se19.team_g.project_rbsg.configuration.FXMLLoaderFactory;
import de.uniks.se19.team_g.project_rbsg.configuration.SceneManagerConfig;
import de.uniks.se19.team_g.project_rbsg.configuration.LocaleConfig;
import de.uniks.se19.team_g.project_rbsg.lobby.chat.LobbyChatClient;
import de.uniks.se19.team_g.project_rbsg.RootController;
import de.uniks.se19.team_g.project_rbsg.waiting_room.event.GameEventManager;
import de.uniks.se19.team_g.project_rbsg.chat.*;
import de.uniks.se19.team_g.project_rbsg.chat.ui.*;
import de.uniks.se19.team_g.project_rbsg.lobby.core.*;
import de.uniks.se19.team_g.project_rbsg.lobby.game.*;
import de.uniks.se19.team_g.project_rbsg.lobby.model.*;
import de.uniks.se19.team_g.project_rbsg.lobby.system.*;
import de.uniks.se19.team_g.project_rbsg.model.*;
import de.uniks.se19.team_g.project_rbsg.server.rest.*;
import de.uniks.se19.team_g.project_rbsg.server.websocket.*;
import io.rincl.*;
import io.rincl.resourcebundle.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;
import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.context.*;
import org.springframework.context.annotation.*;
import org.springframework.lang.*;
import org.springframework.test.context.*;
import org.springframework.test.context.junit4.*;
import org.springframework.web.client.*;
import org.testfx.framework.junit.*;

import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        FXMLLoaderFactory.class,
        TranslationButtonsTest.ContextConfiguration.class,
        ChatBuilder.class,
        GameProvider.class,
        UserProvider.class,
        SceneManager.class,
        JoinGameManager.class,
        CreateGameFormBuilder.class,
        CreateGameController.class,
        LobbyViewController.class,
        MusicManager.class,
        ApplicationState.class,
        SceneManagerConfig.class,
        AlertBuilder.class,
        LocaleConfig.class
})
public class TranslationButtonsTest extends ApplicationTest
{
    @Autowired
    ApplicationContext context;

    @Override
    public void start(final Stage stage) {
        Rincl.setDefaultResourceI18nConcern(new ResourceBundleResourceI18nConcern());

        @SuppressWarnings("unchecked")
        final Scene scene = new Scene(((ViewComponent<RootController>) context.getBean("lobbyScene")).getRoot(), 1200 ,840);

        stage.setScene(scene);
        stage.show();
        stage.toFront();
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
                    return new ArrayList<>();
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
        public GameEventManager gameEventManager() {
            return new GameEventManager(new WebSocketClient()) {
                @Override
                public void startSocket(@NonNull final String gameID, @NonNull final String armyID) {
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
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.context = applicationContext;
        }
    }

    @Test
    public void TranslationsTest() {
        Button enButton = lookup("#enButton").query();
        Button deButton = lookup("#deButton").query();
        Button createGameButton = lookup("#createGameButton").query();

        assertEquals("EN", enButton.getText());
        assertEquals("DE", deButton.getText());

        clickOn("#deButton");

        assertEquals("Spiel erstellen", createGameButton.getText());

        clickOn("#enButton");

        assertEquals("Create game", createGameButton.getText());
    }

}
