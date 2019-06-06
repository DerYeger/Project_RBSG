package de.uniks.se19.team_g.project_rbsg.login;

import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.configuration.FXMLLoaderFactory;
import de.uniks.se19.team_g.project_rbsg.model.User;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.server.rest.LoginManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.RegistrationManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.lang.NonNull;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.testfx.framework.junit.ApplicationTest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

/**
 * @author Keanu Stückrad
 * @author Jan Müller
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        FXMLLoaderFactory.class,
        LoginFormController.class,
        LoginFormBuilder.class,
        LoginFormController.class,
        SplashImageBuilder.class,
        LoginSceneBuilder.class,
        SceneManager.class,
        UserProvider.class,
        TitleViewBuilder.class,
        TitleViewController.class,
        LoginFormControllerTestHttpError.ContextConfiguration.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LoginFormControllerTestHttpError extends ApplicationTest {

    @Autowired
    private LoginFormBuilder loginFormBuilder;

    private static boolean switchedToLobby = false;

    @TestConfiguration
    static class ContextConfiguration implements ApplicationContextAware {

        private static final String BODY = "{\"status\":\"failure\", \"message\":\"Service Unavailable\"}";

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
        public LoginManager loginManager() {
            return new LoginManager(new RestTemplate()) {
                @Override
                @SuppressWarnings("unchecked")
                public CompletableFuture onLogin(User user) {
                    return CompletableFuture.failedFuture(
                            new HttpClientErrorException(HttpStatus.BAD_REQUEST, "status message", BODY.getBytes(), StandardCharsets.UTF_8)
                    );
                }
            };
        }
        @Bean
        public RegistrationManager registrationManager() {
            return new RegistrationManager(new RestTemplate()) {
                @Override
                @SuppressWarnings("unchecked")
                public CompletableFuture onRegistration(User user) {
                    return CompletableFuture.failedFuture(
                            new HttpClientErrorException(HttpStatus.BAD_REQUEST, "status message", BODY.getBytes(), StandardCharsets.UTF_8)
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

        @Bean
        public RestTemplate restTemplate(){
            return new RestTemplate(getClientHttpRequestFactory());
        }

        private ClientHttpRequestFactory getClientHttpRequestFactory() {
            int timeOut = 10000;
            HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
            clientHttpRequestFactory.setConnectTimeout(timeOut);
            clientHttpRequestFactory.setReadTimeout(timeOut);
            return clientHttpRequestFactory;
        }

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

            this.context = applicationContext;
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
        final Label errorMessage = lookup("#errorMessage").query();
        final String expectedErrorMessage = "Service Unavailable";
        Assert.assertEquals(expectedErrorMessage, errorMessage.getText());
        Assert.assertFalse(switchedToLobby);
    }

    @Test
    public void registrationTestFailureNoConnection() {

        final Button registrationButton = lookup("#registration-button").queryButton();
        Assert.assertNotNull(registrationButton);

        clickOn(registrationButton);
        final Label errorMessage = lookup("#errorMessage").query();
        final String expectedErrorMessage = "Service Unavailable";
        Assert.assertEquals(expectedErrorMessage, errorMessage.getText());
        Assert.assertFalse(switchedToLobby);
    }

}




