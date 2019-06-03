package de.uniks.se19.team_g.project_rbsg.login;


import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.configuration.JavaConfig;
import de.uniks.se19.team_g.project_rbsg.ingame.IngameSceneBuilder;
import de.uniks.se19.team_g.project_rbsg.ingame.IngameViewBuilder;
import de.uniks.se19.team_g.project_rbsg.ingame.IngameViewController;
import de.uniks.se19.team_g.project_rbsg.lobby.core.LobbySceneBuilder;
import de.uniks.se19.team_g.project_rbsg.lobby.core.ui.LobbyViewBuilder;
import de.uniks.se19.team_g.project_rbsg.model.GameProvider;
import de.uniks.se19.team_g.project_rbsg.server.rest.LoginManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.RegistrationManager;
import de.uniks.se19.team_g.project_rbsg.model.User;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;

import de.uniks.se19.team_g.project_rbsg.termination.Terminator;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextInputControl;
import javafx.stage.Stage;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.testfx.framework.junit.ApplicationTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Keanu Stückrad
 */

@RunWith(SpringJUnit4ClassRunner .class)
@ContextConfiguration(classes = {
        JavaConfig.class,
        LoginFormController.class,
        LoginFormBuilder.class,
        SplashImageBuilder.class,
        LoginSceneBuilder.class,
        LoginFormControllerTestInvalidCredentialsError.ContextConfiguration.class,
        LobbySceneBuilder.class,
        LobbyViewBuilder.class,
        UserProvider.class,
        IngameSceneBuilder.class,
        IngameViewBuilder.class,
        IngameViewController.class,
        GameProvider.class,
        UserProvider.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LoginFormControllerTestInvalidCredentialsError extends ApplicationTest {

    @Autowired
    private LoginFormBuilder loginFormBuilder;

    private static boolean switchedToLobby = false;

    @TestConfiguration
    static class ContextConfiguration {
        @Bean
        public LoginManager loginManager() {
            return new LoginManager(new RestTemplate() {
                @Override
                public <T> T postForObject(String url, @Nullable Object request, Class<T> responseType, Object... uriVariables) throws RestClientException {
                    Assert.assertTrue(request instanceof User);
                    Assert.assertEquals(url, "https://rbsg.uniks.de/api/user/login");
                    Map<String, Object> testAnswer = new HashMap<>();
                    testAnswer.put("status", "failure");
                    testAnswer.put("message", "Invalid Credentials");
                    testAnswer.put("data", null);
                    return (T) testAnswer;
                }
            });
        }
        @Bean
        public RegistrationManager registrationManager() {
            return new RegistrationManager(new RestTemplate() {
                @Override
                public <T> T postForObject(String url, @Nullable Object request, Class<T> responseType, Object... uriVariables) throws RestClientException {
                    Assert.assertTrue(request instanceof User);
                    Assert.assertEquals(url, "https://rbsg.uniks.de/api/user");
                    Map<String, Object> testAnswer = new HashMap<>();
                    testAnswer.put("status", "failure");
                    testAnswer.put("message", "Name already taken");
                    testAnswer.put("data", null);
                    return (T) testAnswer;
                }
            });
        }
        @Bean
        public SceneManager sceneManager() {
            return new SceneManager(new Terminator()) {
                @Override
                public void setLobbyScene() {
                    switchedToLobby = true;
                }
            };
        }
    }

    @Override
    public void start(@NonNull final Stage stage) throws IOException {
        Node testLoginForm = loginFormBuilder.getLoginForm();
        Assert.assertNotNull(testLoginForm);
        final Scene scene = new Scene((Parent) testLoginForm);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void loginTestFailureInvalidCredentialsAlert() {
        final TextInputControl nameInput = lookup("#name-field").queryTextInputControl();
        Assert.assertNotNull(nameInput);
        final TextInputControl passwordInput = lookup("#password-field").queryTextInputControl();
        Assert.assertNotNull(passwordInput);
        final Button loginButton = lookup("#login-button").queryButton();
        Assert.assertNotNull(loginButton);

        clickOn(nameInput);
        write("WrongName");
        Assert.assertEquals("WrongName", nameInput.getText());

        clickOn(passwordInput);
        write("falsePassword");
        Assert.assertEquals("falsePassword", passwordInput.getText());

        clickOn(loginButton);
        Set<Node> popDialogs = lookup(p -> p instanceof DialogPane).queryAll();
        Assert.assertEquals(popDialogs.size(), 1);
        Node alert = lookup("Login failed").query();
        Assert.assertNotNull(alert);
        Assert.assertFalse(switchedToLobby);
    }

    @Test
    public void registrationTestFailureNameAlreadyTaken() {
        final TextInputControl nameInput = lookup("#name-field").queryTextInputControl();
        Assert.assertNotNull(nameInput);
        final TextInputControl passwordInput = lookup("#password-field").queryTextInputControl();
        Assert.assertNotNull(passwordInput);
        final Button registrationButton = lookup("#registration-button").queryButton();
        Assert.assertNotNull(registrationButton);

        clickOn(nameInput);
        write("MasterChief");
        Assert.assertEquals("MasterChief", nameInput.getText());

        clickOn(passwordInput);
        write("john-117");
        Assert.assertEquals("john-117", passwordInput.getText());

        clickOn(registrationButton);
        Set<Node> popDialogs = lookup(p -> p instanceof DialogPane).queryAll();
        Assert.assertEquals(popDialogs.size(), 1);
        Node alert = lookup("Registration failed").query();
        Assert.assertNotNull(alert);
        Assert.assertFalse(switchedToLobby);
    }

}




