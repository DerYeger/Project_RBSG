package de.uniks.se19.team_g.project_rbsg.controller;

import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.configuration.JavaConfig;
import de.uniks.se19.team_g.project_rbsg.lobby.core.LobbySceneBuilder;
import de.uniks.se19.team_g.project_rbsg.lobby.core.ui.LobbyViewBuilder;
import de.uniks.se19.team_g.project_rbsg.server.rest.LoginManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.RegistrationManager;
import de.uniks.se19.team_g.project_rbsg.login.LoginFormBuilder;
import de.uniks.se19.team_g.project_rbsg.login.LoginFormController;
import de.uniks.se19.team_g.project_rbsg.login.LoginSceneBuilder;
import de.uniks.se19.team_g.project_rbsg.login.SplashImageBuilder;
import de.uniks.se19.team_g.project_rbsg.model.User;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
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
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * @author Keanu Stückrad
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        JavaConfig.class,
        LoginFormController.class,
        LoginFormBuilder.class,
        SplashImageBuilder.class,
        LoginSceneBuilder.class,
        LoginFormControllerTestHttpError.ContextConfiguration.class,
        LobbySceneBuilder.class,
        LobbyViewBuilder.class,
        UserProvider.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LoginFormControllerTestHttpError extends ApplicationTest {

    @Autowired
    private LoginFormBuilder loginFormBuilder;

    private static boolean switchedToLobby = false;

    @TestConfiguration
    static class ContextConfiguration {
        @Bean
        public LoginManager loginManager() {
            return new LoginManager(new RestTemplate()) {
                @Override
                public CompletableFuture onLogin(User user) {
                    return CompletableFuture.failedFuture(
                            new RestClientResponseException(
                                    "Service Unavailable",
                                    HttpStatus.SERVICE_UNAVAILABLE.value(),
                                    "Unauthorized",
                                    null, null, null
                            )
                    );
                }
            };
        }
        @Bean
        public RegistrationManager registrationManager() {
            return new RegistrationManager(new RestTemplate()) {
                @Override
                public CompletableFuture onRegistration(User user) {
                    return CompletableFuture.failedFuture(
                            new RestClientResponseException(
                                    "Service Unavailable",
                                    HttpStatus.SERVICE_UNAVAILABLE.value(),
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
    public void loginTestFailureNoConnection() {

        final Button loginButton = lookup("#login-button").queryButton();
        Assert.assertNotNull(loginButton);

        clickOn(loginButton);
        Set<Node> popDialogs = lookup(p -> p instanceof DialogPane).queryAll();
        Assert.assertEquals(popDialogs.size(), 1);
        Node alert = lookup("Login failed").query();
        Assert.assertNotNull(alert);
        Assert.assertFalse(switchedToLobby);
    }

    @Test
    public void registrationTestFailureNoConnection() {

        final Button registrationButton = lookup("#registration-button").queryButton();
        Assert.assertNotNull(registrationButton);

        clickOn(registrationButton);
        Set<Node> popDialogs = lookup(p -> p instanceof DialogPane).queryAll();
        Assert.assertEquals(popDialogs.size(), 1);
        Node alert = lookup("Registration failed").query();
        Assert.assertNotNull(alert);
        Assert.assertFalse(switchedToLobby);
    }

}




