package de.uniks.se19.team_g.project_rbsg.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.configuration.ApplicationStateInitializer;
import de.uniks.se19.team_g.project_rbsg.configuration.FXMLLoaderFactory;
import de.uniks.se19.team_g.project_rbsg.server.rest.LoginManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.RegistrationManager;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import io.rincl.*;
import io.rincl.resourcebundle.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * @author Keanu Stückrad
 * @author Jan Müller
 */
@RunWith(SpringJUnit4ClassRunner .class)
@ContextConfiguration(classes = {
        FXMLLoaderFactory.class,
        LoginFormController.class,
        LoginFormBuilder.class,
        LoginFormController.class,
        SplashImageBuilder.class,
        UserProvider.class,
        TitleViewBuilder.class,
        TitleViewController.class,
        LoginFormControllerTestSuccess.ContextConfiguration.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LoginFormControllerTestSuccess extends ApplicationTest {

    @Autowired
    private LoginFormBuilder loginFormBuilder;

    private static boolean switchedToLobby = false;

    static private ApplicationStateInitializer initializer;

    @TestConfiguration
    static class ContextConfiguration implements ApplicationContextAware {

        private ApplicationContext context;

        @Bean
        public ApplicationStateInitializer stateInitializer() {
            initializer = Mockito.mock(ApplicationStateInitializer.class);
            Mockito.when(initializer.initialize()).thenReturn(CompletableFuture.completedFuture(null));
            return initializer;
        }

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
                public <T> ResponseEntity<T> exchange(String url, HttpMethod method, @Nullable HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables) throws RestClientException {
                    Assert.assertEquals(url, "https://rbsg.uniks.de/api/user/login");
                    final ObjectNode body = new ObjectMapper().createObjectNode();
                    body.put("status", "success");
                    body.put("message", "");
                    body.with("data").put("name", "test").put("userKey", "12345");

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
                    body.put("status", "success");
                    body.put("message", "");
                    body.with("data").put("name", "test").put("userKey", "12345");

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
                public void setScene(@NonNull final SceneIdentifier sceneIdentifier, @NonNull final boolean useCaching, @Nullable final SceneIdentifier cacheIdentifier) {
                    switchedToLobby = sceneIdentifier.equals(SceneIdentifier.LOBBY);
                }
            };
        }

        @Override
        public void setApplicationContext(@Nonnull ApplicationContext applicationContext) throws BeansException {
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
    public void loginTestSuccess() {
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
        Assert.assertTrue(switchedToLobby);

        Mockito.verify(initializer, Mockito.times(1)).initialize();
    }

    @Test
    public void registrationTestSuccess() {
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
        Assert.assertTrue(switchedToLobby);
    }

}




