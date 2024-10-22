package de.uniks.se19.team_g.project_rbsg.lobby.core;

import de.uniks.se19.team_g.project_rbsg.MusicManager;
import de.uniks.se19.team_g.project_rbsg.scene.SceneManager;
import de.uniks.se19.team_g.project_rbsg.overlay.alert.AlertBuilder;
import de.uniks.se19.team_g.project_rbsg.configuration.SceneManagerConfig;
import de.uniks.se19.team_g.project_rbsg.scene.ViewComponent;
import de.uniks.se19.team_g.project_rbsg.chat.ChatClient;
import de.uniks.se19.team_g.project_rbsg.chat.ChatController;
import de.uniks.se19.team_g.project_rbsg.chat.command.ChatCommandManager;
import de.uniks.se19.team_g.project_rbsg.chat.ui.ChatBuilder;
import de.uniks.se19.team_g.project_rbsg.chat.ui.ChatTabManager;
import de.uniks.se19.team_g.project_rbsg.configuration.ApplicationState;
import de.uniks.se19.team_g.project_rbsg.configuration.FXMLLoaderFactory;
import de.uniks.se19.team_g.project_rbsg.configuration.LocaleConfig;
import de.uniks.se19.team_g.project_rbsg.lobby.chat.LobbyChatClient;
import de.uniks.se19.team_g.project_rbsg.lobby.core.ui.GameListViewCell;
import de.uniks.se19.team_g.project_rbsg.lobby.core.ui.LobbyViewController;
import de.uniks.se19.team_g.project_rbsg.overlay.credits.CreditsBuilder;
import de.uniks.se19.team_g.project_rbsg.lobby.game.CreateGameFormBuilder;
import de.uniks.se19.team_g.project_rbsg.lobby.game.GameManager;
import de.uniks.se19.team_g.project_rbsg.lobby.system.SystemMessageManager;
import de.uniks.se19.team_g.project_rbsg.model.GameProvider;
import de.uniks.se19.team_g.project_rbsg.model.User;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.overlay.menu.MenuBuilder;
import de.uniks.se19.team_g.project_rbsg.server.rest.DefaultLogoutManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.JoinGameManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.RESTClient;
import de.uniks.se19.team_g.project_rbsg.server.websocket.WebSocketClient;
import javafx.beans.property.Property;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;
import org.testfx.framework.junit.ApplicationTest;

import javax.annotation.Nonnull;
import java.util.Locale;

import static org.junit.Assert.assertNotNull;

/**
 * @author Georg Siebert
 */


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        FXMLLoaderFactory.class,
        LobbyBuilderTest.ContextConfiguration.class,
        ChatBuilder.class,
        GameProvider.class,
        SceneManager.class,
        JoinGameManager.class,
        ApplicationState.class,
        SceneManagerConfig.class,
        GameListViewCell.class,
        LocaleConfig.class
})
public class LobbyBuilderTest extends ApplicationTest
{

    @Autowired
    private ApplicationContext context;

    private Node lobbyView;

    @Override
    public void start(@Nonnull final Stage stage)
    {
        @SuppressWarnings("unchecked")
        ViewComponent<LobbyViewController> components = (ViewComponent<LobbyViewController>) context.getBean("lobbyScene");
        lobbyView = components.getRoot();


        final Scene scene = new Scene((Parent) lobbyView,1280 ,720);
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
        public LobbyViewController lobbyViewController(
                ApplicationState appState,
                GameProvider gameProvider,
                UserProvider userProvider,
                SceneManager sceneManager,
                JoinGameManager joinGameManager,
                ObjectFactory<GameListViewCell> cellFactory,
                Property<Locale> selectedLocale
        ) {
            return new LobbyViewController(
                    userProvider,
                    sceneManager,
                    new PlayerManager(new RESTClient(new RestTemplate()), userProvider()),
                    new GameManager(new RESTClient(new RestTemplate()), userProvider()),
                    new SystemMessageManager(new WebSocketClient()),
                    chatController(),
                    new LobbyChatClient(new WebSocketClient(), userProvider()),
                    new CreateGameFormBuilder(new FXMLLoader()),
                    new DefaultLogoutManager(new RESTClient(new RestTemplate())),
                    new AlertBuilder(sceneManager),
                    new MenuBuilder(sceneManager, null, new CreditsBuilder(sceneManager, null), new MusicManager()),
                    cellFactory,
                    selectedLocale,
                    new EmailManager(appState),
                    appState,
                    null
            ) {
                @Override
                public void initialize()
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
        public ChatController chatController() {
            return  new ChatController(new UserProvider(), new ChatCommandManager(), new ChatTabManager()) {
                @Override
                public void init(@Nonnull final TabPane chatPane, @Nonnull final ChatClient chatClient)
                {
                }
            };
        }

        @Override
        public void setApplicationContext(@Nonnull ApplicationContext applicationContext) throws BeansException {
            this.context = applicationContext;
        }
    }

}
