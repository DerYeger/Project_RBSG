package de.uniks.se19.team_g.project_rbsg.apis;

import de.uniks.se19.team_g.project_rbsg.JavaConfig;
import de.uniks.se19.team_g.project_rbsg.controller.LoginFormController;
import de.uniks.se19.team_g.project_rbsg.view.LoginFormBuilder;

import de.uniks.se19.team_g.project_rbsg.view.LoginSceneBuilder;
import de.uniks.se19.team_g.project_rbsg.view.SplashImageBuilder;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputControl;
import javafx.stage.Stage;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testfx.framework.junit.ApplicationTest;

import java.io.IOException;

/**
 * @author Keanu Stückrad
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JavaConfig.class, LoginFormBuilder.class, LoginFormController.class, RegistrationManager.class, LoginManager.class})
public class LoginMangerTest extends ApplicationTest {

    // private LoginManager loginManager;

    @Autowired
    private ApplicationContext context;

    private static Scene scene;

    @Override
    public void start(@NonNull final Stage stage) throws IOException {
        final Node loginForm = context.getBean(LoginFormBuilder.class).getLoginForm();
        Assert.assertNotNull(loginForm);

        if (scene == null) {
            scene = new Scene((Parent) loginForm);
        }

        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void test() {

        //loginManager = setLoginManager("success", "", (JsonObject) Json.createObjectBuilder().add("userKey", "ohYesYoureSuchAGodDamnKey"));

        final TextInputControl nameInput = lookup("#name-field").queryTextInputControl();
        Assert.assertNotNull(nameInput);
        final TextInputControl passwordInput = lookup("#password-field").queryTextInputControl();
        Assert.assertNotNull(passwordInput);
        final Button loginButton = lookup("#login-button").queryButton();
        Assert.assertNotNull(loginButton);

        clickOn(nameInput);
        write("MasterChief");

        clickOn(passwordInput);
        write("john-117");

        clickOn(loginButton);
    }

    @Test
    public void loginTestFailureInvalidCredentialsAlert() throws JSONException {

        //loginManager = setLoginManager("failure", "Invalid credentials", (JsonObject) Json.createObjectBuilder());

        final TextInputControl nameInput = lookup("#name-field").queryTextInputControl();
        Assert.assertNotNull(nameInput);
        final TextInputControl passwordInput = lookup("#password-field").queryTextInputControl();
        Assert.assertNotNull(passwordInput);
        final Button loginButton = lookup("#login-button").queryButton();
        Assert.assertNotNull(loginButton);

        clickOn(nameInput);
        write("WrongName");

        clickOn(passwordInput);
        write("thisIsNotARightPassword");

        clickOn(loginButton);
    }

    @Test
    public void loginTestFailureNoConnection() throws JSONException {

        // loginManager = setLoginManager("failure", "No server connection", Json.createObjectBuilder().build());

        final Button loginButton = lookup("#login-button").queryButton();
        Assert.assertNotNull(loginButton);

        clickOn(loginButton);
    }

}
