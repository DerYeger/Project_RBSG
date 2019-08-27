package de.uniks.se19.team_g.project_rbsg.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.uniks.se19.team_g.project_rbsg.overlay.alert.AlertBuilder;
import de.uniks.se19.team_g.project_rbsg.scene.SceneConfiguration;
import de.uniks.se19.team_g.project_rbsg.scene.SceneManager;
import de.uniks.se19.team_g.project_rbsg.configuration.ApplicationStateInitializer;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.server.rest.LoginManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.RegistrationManager;
import io.rincl.*;
import io.rincl.resourcebundle.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.testfx.framework.junit.ApplicationTest;

import java.io.IOException;

import static org.mockito.Mockito.mock;

/**
 * @author Keanu Stückrad
 * @author Jan Müller
 */
@RunWith(SpringJUnit4ClassRunner .class)
@ContextConfiguration(classes = {
        LoginFormController.class,
        LoginFormBuilder.class,
        LoginFormController.class,
        SplashImageBuilder.class,
        SceneManager.class,
        UserProvider.class,
        LoginFormControllerTestInvalidCredentialsError.ContextConfiguration.class,
        TitleViewBuilder.class,
        TitleViewController.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LoginFormControllerTestInvalidCredentialsError extends ApplicationTest {

    @Autowired
    private LoginFormBuilder loginFormBuilder;

    private static boolean switchedToLobby = false;

    @TestConfiguration
    static class ContextConfiguration  implements ApplicationContextAware {

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
        public ApplicationStateInitializer stateInitializer() {
            return Mockito.mock(ApplicationStateInitializer.class);
        }

        @Bean
        public LoginManager loginManager() {
            return new LoginManager(new RestTemplate() {
                @Override
                public <T> ResponseEntity<T> exchange(String url, HttpMethod method, @Nullable HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables) throws RestClientException {
                    Assert.assertEquals(url, "https://rbsg.uniks.de/api/user/login");
                    final ObjectNode body = new ObjectMapper().createObjectNode();
                    body.put("status", "failure");
                    body.put("message", "Invalid Credentials");
                    body.with("data");

                    ResponseEntity<ObjectNode> response = new ResponseEntity<>(body, HttpStatus.OK);
                    @SuppressWarnings("unchecked")
                    ResponseEntity<T> castedResponse = (ResponseEntity<T>) response;
                    return castedResponse;
                }
            });
        }
        @Bean
        public RegistrationManager registrationManager() {
            return new RegistrationManager(new RestTemplate() {
                @Override
                public <T> ResponseEntity<T> exchange(String url, HttpMethod method, @Nullable HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables) throws RestClientException {
                    Assert.assertEquals(url, "https://rbsg.uniks.de/api/user");
                    final ObjectNode body = new ObjectMapper().createObjectNode();
                    body.put("status", "failure");
                    body.put("message", "Name already taken");
                    body.with("data");

                    ResponseEntity<ObjectNode> response = new ResponseEntity<>(body, HttpStatus.OK);
                    @SuppressWarnings("unchecked")
                    ResponseEntity<T> castedResponse = (ResponseEntity<T>) response;
                    return castedResponse;
                }
            });
        }

        @Bean
        public SceneManager sceneManager() {
            return new SceneManager() {
                @Override
                public void setScene(@NonNull final SceneConfiguration sceneConfiguration) {
                    switchedToLobby = sceneConfiguration.getSceneIdentifier().equals(SceneIdentifier.LOBBY);
                }
            };
        }

        @Bean
        public AlertBuilder alertBuilder() {
            return mock(AlertBuilder.class);
        }

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.context = applicationContext;
        }
    }

    @Override
    public void start(@NonNull final Stage stage) throws IOException {
        Rincl.setDefaultResourceI18nConcern(new ResourceBundleResourceI18nConcern());
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
        write("1");
        Assert.assertEquals("1", nameInput.getText());

        type(KeyCode.TAB);
        write("1");
        Assert.assertEquals("1", passwordInput.getText());

        clickOn(loginButton);
        final Label errorMessage = lookup("#errorMessage").query();
        final String expectedErrorMessage = "Invalid Credentials";
        Assert.assertEquals(expectedErrorMessage, errorMessage.getText());
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
        write("1");
        Assert.assertEquals("1", nameInput.getText());

        type(KeyCode.TAB);
        write("1");
        Assert.assertEquals("1", passwordInput.getText());

        clickOn(registrationButton);
        final Label errorMessage = lookup("#errorMessage").query();
        final String expectedErrorMessage = "Name already taken";
        Assert.assertEquals(expectedErrorMessage, errorMessage.getText());
        Assert.assertFalse(switchedToLobby);
    }

}




