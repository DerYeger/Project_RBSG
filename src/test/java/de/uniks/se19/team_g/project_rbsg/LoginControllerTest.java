package de.uniks.se19.team_g.project_rbsg;

import de.uniks.se19.team_g.project_rbsg.Lobby.UI.Views.LobbyViewBuilder;
import de.uniks.se19.team_g.project_rbsg.apis.RegistrationManager;
import de.uniks.se19.team_g.project_rbsg.controller.LoginFormController;
import de.uniks.se19.team_g.project_rbsg.model.User;
import de.uniks.se19.team_g.project_rbsg.view.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.service.query.NodeQuery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@RunWith(SpringJUnit4ClassRunner .class)
@ContextConfiguration(classes = {
        JavaConfig.class,
        LoginFormController.class,
        LoginFormBuilder.class,
        SplashImageBuilder.class,
        LoginSceneBuilder.class,
        LoginControllerTest.ContextConfiguration.class,
        LobbySceneBuilder.class,
        LobbyViewBuilder.class
})
public class LoginControllerTest extends ApplicationTest {

    @Autowired
    private LoginFormBuilder loginFormBuilder;

    private static boolean switchedToLobby = false;

    @TestConfiguration
    static class ContextConfiguration {

        @Bean
        public RegistrationManager registrationManager() {
            return new RegistrationManager(new RestTemplate()) {
                @Override
                public CompletableFuture onRegistration(User user) {
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

        @Bean
        public SceneManager sceneManager() {
            return new SceneManager() {
                @Override
                public void setLoginScene() {
                    switchedToLobby = true;
                }
            };
        }
    }




    public void start(@NonNull Stage stage) throws IOException {

        Node testLoginForm = loginFormBuilder.getLoginForm();
        final Scene scene = new Scene((Parent) testLoginForm);
        stage.setScene(scene);
        stage.show();
    }


    @Test
    public void test() {
        final TextInputControl nameField = lookup("#nameField").queryTextInputControl();
        final TextInputControl passwordField = lookup("#passwordField").queryTextInputControl();
        final Button registerButton = lookup("#registerButton").queryButton();
        Assert.assertNotNull(nameField);
        Assert.assertNotNull(nameField);

        final String testName = "Tom Waits";
        clickOn(nameField);
        write(testName);
        press(KeyCode.ENTER);
        release(KeyCode.ENTER);

        final String password = "geheim";
        clickOn(passwordField);
        write(password);
        press(KeyCode.ENTER);
        release(KeyCode.ENTER);

        clickOn(registerButton);
        Set<Node> popDialgos = lookup(p -> p instanceof DialogPane).queryAll();
        Assert.assertEquals(popDialgos.size(), 1);
        Node alert = lookup("Fehler bei der Registrierung").query();
        Assert.assertNotNull(alert);


        Assert.assertFalse(switchedToLobby);
    }
}



