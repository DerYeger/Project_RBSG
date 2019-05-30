package de.uniks.se19.team_g.project_rbsg.LobbyTests;

import de.uniks.se19.team_g.project_rbsg.JavaConfig;
import de.uniks.se19.team_g.project_rbsg.apis.GameCreator;
import de.uniks.se19.team_g.project_rbsg.controller.CreateGameController;
import de.uniks.se19.team_g.project_rbsg.model.Game;
import de.uniks.se19.team_g.project_rbsg.model.GameBuilder;
import de.uniks.se19.team_g.project_rbsg.model.User;
import de.uniks.se19.team_g.project_rbsg.view.CreateGameFormBuilder;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.testfx.framework.junit.ApplicationTest;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes ={JavaConfig.class, CreateGameController.class, CreateGameFormBuilder.class, GameBuilder.class, CreateGameControllerTest.ContextConfiguration.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CreateGameControllerTest extends ApplicationTest {

        @Autowired
        private CreateGameFormBuilder createGameFormBuilder;

        @TestConfiguration
        static class ContextConfiguration {

            @Bean
            public GameCreator gameCreator() {
                return new GameCreator(new RestTemplate()) {
                    @Override
                    public CompletableFuture sendGameRequest(User user, Game game) {
                        return CompletableFuture.failedFuture(
                                new RestClientResponseException(
                                        "Invalid Credentials",
                                        HttpStatus.UNAUTHORIZED.value(),
                                        "Unauthorized",
                                        null, null, null
                                )
                        );
                    }
                };
            }
        }
    private Scene scene;

    @Override
    public void start(@NonNull Stage stage) throws IOException{
        final Node gameForm = createGameFormBuilder.getCreateGameForm();
        this.scene = new Scene((Parent) gameForm);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void testRejectedFutureHandling(){
        final TextInputControl gameNameInput = lookup("#gameName").queryTextInputControl();
        Assert.assertNotNull(gameNameInput);
        final RadioButton fourPlayerButton = lookup("#fourPlayers").query();
        Assert.assertNotNull(fourPlayerButton);
        final Button createGameButton = lookup("#create").queryButton();
        clickOn(fourPlayerButton);
        clickOn(createGameButton);
        final Node alert = lookup("Fehler: Keine Verbindung zum Server moeglich").query();
        Assert.assertNotNull(alert);
    }

    @Test
    public void testFormInputGameName(){
        final TextInputControl gameNameInput = lookup("#gameName").queryTextInputControl();
        Assert.assertNotNull(gameNameInput);
        final RadioButton twoPlayerButton = lookup("#twoPlayers").query();
        Assert.assertNotNull(twoPlayerButton);
        final Button createGameButton = lookup("#create").queryButton();

        clickOn(gameNameInput);
        eraseText(20);
        clickOn(gameNameInput);
        eraseText(20);
        press(KeyCode.ENTER);
        release(KeyCode.ENTER);

        clickOn(twoPlayerButton);

        clickOn(createGameButton);
        final Node alert = lookup("Fehler: Fehler bei Eingabeinformation").query();
        Assert.assertNotNull(alert);
    }

    @Test
    public void testNoNumberOfPlayersSelected(){
        final TextInputControl gameNameInput = lookup("#gameName").queryTextInputControl();
        Assert.assertNotNull(gameNameInput);
        final Button createGameButton = lookup("#create").queryButton();

        clickOn(createGameButton);
        final Node alert = lookup("Fehler: Fehler bei Eingabeinformation").query();
        Assert.assertNotNull(alert);
    }
}
