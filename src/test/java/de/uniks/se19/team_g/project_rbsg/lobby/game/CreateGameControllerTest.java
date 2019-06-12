package de.uniks.se19.team_g.project_rbsg.lobby.game;

import de.uniks.se19.team_g.project_rbsg.model.Game;
import de.uniks.se19.team_g.project_rbsg.model.User;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.server.rest.GameCreator;
import io.rincl.*;
import io.rincl.resourcebundle.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
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
import org.springframework.web.client.RestTemplate;
import org.testfx.framework.junit.ApplicationTest;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes ={
        CreateGameController.class,
        CreateGameFormBuilder.class,
        UserProvider.class,
        CreateGameControllerTest.ContextConfiguration.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CreateGameControllerTest extends ApplicationTest {

        @Autowired
        private CreateGameFormBuilder createGameFormBuilder;

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
        final TextInputControl gameNameInput = lookup("#gameName").queryTextInputControl();
        Assert.assertNotNull(gameNameInput);
        final ToggleButton fourPlayerButton = lookup("#fourPlayers").query();
        Assert.assertNotNull(fourPlayerButton);
        final Button createGameButton = lookup("#create").queryButton();
        final String newGameName = "Noodles";
        clickOn(gameNameInput);
        write(newGameName);
        clickOn(fourPlayerButton);
        clickOn(createGameButton);
        final Node alert = lookup("Fehler: Keine Verbindung zum Server moeglich").query();
        Assert.assertNotNull(alert);
    }

    @Test
    public void testFormInputGameName(){
        final TextInputControl gameNameInput = lookup("#gameName").queryTextInputControl();
        Assert.assertNotNull(gameNameInput);
        final ToggleButton twoPlayerButton = lookup("#twoPlayers").query();
        Assert.assertNotNull(twoPlayerButton);
        final Button createGameButton = lookup("#create").queryButton();

        press(KeyCode.ENTER);
        release(KeyCode.ENTER);

        clickOn(twoPlayerButton);

        clickOn(createGameButton);
        final Node alert = lookup("Fehler: Fehler bei Eingabeinformation").query();
        Assert.assertNotNull(alert);
    }

}
