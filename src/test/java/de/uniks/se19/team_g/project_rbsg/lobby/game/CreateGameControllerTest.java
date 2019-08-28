package de.uniks.se19.team_g.project_rbsg.lobby.game;

import de.uniks.se19.team_g.project_rbsg.chat.ChatController;
import de.uniks.se19.team_g.project_rbsg.chat.ui.ChatBuilder;
import de.uniks.se19.team_g.project_rbsg.lobby.chat.LobbyChatClient;
import de.uniks.se19.team_g.project_rbsg.lobby.core.PlayerManager;
import de.uniks.se19.team_g.project_rbsg.lobby.core.ui.GameListViewCell;
import de.uniks.se19.team_g.project_rbsg.lobby.core.ui.LobbyViewController;
import de.uniks.se19.team_g.project_rbsg.lobby.loading_screen.LoadingScreenController;
import de.uniks.se19.team_g.project_rbsg.lobby.loading_screen.LoadingScreenFormBuilder;
import de.uniks.se19.team_g.project_rbsg.lobby.system.SystemMessageManager;
import de.uniks.se19.team_g.project_rbsg.model.GameProvider;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.overlay.alert.AlertBuilder;
import de.uniks.se19.team_g.project_rbsg.overlay.menu.MenuBuilder;
import de.uniks.se19.team_g.project_rbsg.scene.SceneConfiguration;
import de.uniks.se19.team_g.project_rbsg.scene.SceneManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.DefaultLogoutManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.GameCreator;
import de.uniks.se19.team_g.project_rbsg.server.rest.JoinGameManager;
import io.rincl.Rincl;
import io.rincl.resourcebundle.ResourceBundleResourceI18nConcern;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestClientResponseException;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import static de.uniks.se19.team_g.project_rbsg.overlay.alert.AlertBuilder.Text.INVALID_INPUT;
import static de.uniks.se19.team_g.project_rbsg.overlay.alert.AlertBuilder.Text.NO_CONNECTION;
import static de.uniks.se19.team_g.project_rbsg.scene.SceneManager.SceneIdentifier.INGAME;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes ={
        CreateGameController.class,
        CreateGameFormBuilder.class,
        UserProvider.class,
        CreateGameControllerTest.ContextConfiguration.class,
        GameProvider.class,
        LoadingScreenFormBuilder.class,
        LoadingScreenController.class,
        ChatBuilder.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CreateGameControllerTest extends ApplicationTest implements ApplicationContextAware {

    @Autowired
    private CreateGameFormBuilder createGameFormBuilder;

    @Autowired
    private LobbyViewController lobbyViewController;

    @Autowired
    public SceneManager sceneManager;

    @Autowired
    public JoinGameManager joinGameManager;

    @Autowired
    public GameCreator gameCreator;

    private ApplicationContext context;

    private static AlertBuilder.Text lastError;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = context;
    }

    @TestConfiguration
    static class ContextConfiguration implements ApplicationContextAware {

        @Autowired
        SceneManager sceneManager;

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
        @Scope("prototype")
        public LobbyViewController lobbyViewController(){
            @SuppressWarnings("unchecked") final ObjectFactory<GameListViewCell> mock = mock(ObjectFactory.class);
            return new LobbyViewController(
                    mock(UserProvider.class),
                    sceneManager,
                    mock(PlayerManager.class),
                    mock(GameManager.class),
                    mock(SystemMessageManager.class),
                    mock(ChatController.class),
                    mock(LobbyChatClient.class),
                    mock(CreateGameFormBuilder.class),
                    mock(DefaultLogoutManager.class),
                    mock(AlertBuilder.class),
                    mock(MenuBuilder.class),
                    mock,
                    null,
                    null,
                    null,
                    null
            ){
                @Override
                public void setBackgroundImage(){
                    this.mainStackPane = new StackPane();
                }
            };
        }

        @Bean
        public GameCreator gameCreator()
        {
            return Mockito.mock(GameCreator.class);
        }

        @Bean
        public JoinGameManager joinGameManager() {
            return Mockito.mock(JoinGameManager.class);
        }

        @Bean
        public SceneManager sceneManager() {
            return Mockito.mock(SceneManager.class);
        }

        @Bean
        public AlertBuilder alertBuilder() {
            return new AlertBuilder(null) {
                @Override
                public void information(@NonNull final Text text) {
                    lastError = text;
                }
            };
        }

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.context = applicationContext;
        }
    }

    private Scene scene;

    @Override
    public void start(@NonNull Stage stage) throws IOException{
        Rincl.setDefaultResourceI18nConcern(new ResourceBundleResourceI18nConcern());
        final Node gameForm = createGameFormBuilder.getCreateGameForm();
        this.scene = new Scene((Parent) gameForm);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void testRejectedFutureHandling(){

        Mockito.when(gameCreator.sendGameRequest(any(), any())).thenReturn(
            CompletableFuture.failedFuture(
                    new RestClientResponseException(
                            "Invalid Credentials",
                            HttpStatus.UNAUTHORIZED.value(),
                            "Unauthorized",
                            null, null, null
                    )
            )
        );
        createGameFormBuilder.getCreateGameController().setLobbyViewController(lobbyViewController);
        createGameFormBuilder.getCreateGameController().getLobbyViewController().setBackgroundImage();
        final TextInputControl gameNameInput = lookup("#gameName").queryTextInputControl();
        Assert.assertNotNull(gameNameInput);
        final ToggleButton fourPlayerButton = lookup("#fourPlayers").query();
        Assert.assertNotNull(fourPlayerButton);
        final Button createGameButton = lookup("#create").queryButton();
        final String newGameName = "G";
        clickOn(gameNameInput);
        write(newGameName);
        clickOn(fourPlayerButton);
        clickOn(createGameButton);
        WaitForAsyncUtils.waitForFxEvents();
        Assert.assertEquals(NO_CONNECTION, lastError);
    }

    @Test
    public void testFormInputGameName(){

        Mockito.when(
                joinGameManager.joinGame(any(), any())
        ).thenReturn(CompletableFuture.completedFuture(null));

        Mockito.when(gameCreator.sendGameRequest(any(), any())).thenReturn(
                CompletableFuture.completedFuture(
                        new HashMap<>() {{
                            put("status", "success");
                            put("data", Collections.singletonMap("gameId", "nine-nine!!"));
                        }}
                )
        );
        createGameFormBuilder.getCreateGameController().setLobbyViewController(lobbyViewController);
        createGameFormBuilder.getCreateGameController().getLobbyViewController().setBackgroundImage();
        final TextInputControl gameNameInput = lookup("#gameName").queryTextInputControl();
        Assert.assertNotNull(gameNameInput);
        final ToggleButton twoPlayerButton = lookup("#twoPlayers").query();
        Assert.assertNotNull(twoPlayerButton);
        final Button createGameButton = lookup("#create").queryButton();
        Assert.assertNotNull(createGameButton);
        final Button cancelButton = lookup("#cancel").queryButton();
        Assert.assertNotNull(cancelButton);

        press(KeyCode.ENTER);
        release(KeyCode.ENTER);

        Assert.assertEquals(INVALID_INPUT, lastError);

        clickOn(createGameButton);

        Platform.runLater(gameNameInput::requestFocus);
        WaitForAsyncUtils.waitForFxEvents();
        write("gg");

        clickOn(createGameButton);

        Mockito.verify(gameCreator).sendGameRequest(any(), any());
        Mockito.verify(joinGameManager).joinGame(any(), any());
        Mockito.verify(sceneManager).setScene(SceneConfiguration.of(INGAME));
    }

}
