package de.uniks.se19.team_g.project_rbsg.login;

import de.uniks.se19.team_g.project_rbsg.SceneManager;
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
import javafx.scene.control.TextInputControl;
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
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
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

/**
 * @author Keanu Stückrad
 */

@RunWith(SpringJUnit4ClassRunner .class)
@ContextConfiguration(classes = {
        LoginFormController.class,
        LoginFormBuilder.class,
        LoginFormController.class,
        SplashImageBuilder.class,
        LoginSceneBuilder.class,
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
        write("MasterChief");
        Assert.assertEquals("MasterChief", nameInput.getText());

        clickOn(passwordInput);
        write("john-117");
        Assert.assertEquals("john-117", passwordInput.getText());

        clickOn(registrationButton);
        final Label errorMessage = lookup("#errorMessage").query();
        final String expectedErrorMessage = "Name already taken";
        Assert.assertEquals(expectedErrorMessage, errorMessage.getText());
        Assert.assertFalse(switchedToLobby);
    }

}




