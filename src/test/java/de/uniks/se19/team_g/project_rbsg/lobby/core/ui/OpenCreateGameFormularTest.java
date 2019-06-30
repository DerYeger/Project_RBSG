package de.uniks.se19.team_g.project_rbsg.lobby.core.ui;

import de.uniks.se19.team_g.project_rbsg.*;
import de.uniks.se19.team_g.project_rbsg.alert.AlertBuilder;
import de.uniks.se19.team_g.project_rbsg.chat.command.ChatCommandManager;
import de.uniks.se19.team_g.project_rbsg.chat.ui.ChatBuilder;
import de.uniks.se19.team_g.project_rbsg.chat.ui.ChatTabManager;
import de.uniks.se19.team_g.project_rbsg.configuration.ApplicationState;
import de.uniks.se19.team_g.project_rbsg.configuration.FXMLLoaderFactory;
import de.uniks.se19.team_g.project_rbsg.chat.*;
import de.uniks.se19.team_g.project_rbsg.chat.ui.*;
import de.uniks.se19.team_g.project_rbsg.configuration.SceneManagerConfig;
import de.uniks.se19.team_g.project_rbsg.lobby.chat.LobbyChatClient;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.Test;
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
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        FXMLLoaderFactory.class,
        OpenCreateGameFormularTest.ContextConfiguration.class,
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
public class OpenCreateGameFormularTest extends ApplicationTest
{

    @Autowired
    ApplicationContext context;

    @TestConfiguration
    public static class ContextConfiguration implements ApplicationContextAware {

        private ApplicationContext context;

        @Bean
        public ApplicationState appState()
        {
            final ApplicationState appState = new ApplicationState();
            // must be valid for createGame to be selectable
            appState.validArmySelected.set(true);

            return appState;
        }

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
                    return new ArrayList<>();
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

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.context = applicationContext;
        }
    }

    @Override
    public void start(Stage stage) {
        Rincl.setDefaultResourceI18nConcern(new ResourceBundleResourceI18nConcern());
        @SuppressWarnings("unchecked")
        ViewComponent<LobbyViewController> components = (ViewComponent<LobbyViewController>) context.getBean("lobbyScene");

        final Scene scene = new Scene(components.getRoot(),1200 ,840);

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

        // TODO: maybe mount modal root, only if required, and remove it from the scenegraph otherwise.
        assertEquals(3, stackPane.getChildren().size());
    }

}
