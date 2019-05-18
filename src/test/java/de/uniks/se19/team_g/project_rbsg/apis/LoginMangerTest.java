package de.uniks.se19.team_g.project_rbsg.apis;

import de.uniks.se19.team_g.project_rbsg.view.LoginFormBuilder;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputControl;
import javafx.stage.Stage;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.testfx.framework.junit.ApplicationTest;

import java.io.IOException;

/**
 * @author Keanu Stückrad
 */
@Component
public class LoginMangerTest extends ApplicationTest {

    // private LoginManager loginManager;

    @Autowired
    private ApplicationContext context;

    @Override
    public void start(@NonNull final Stage stage) throws IOException {
        final LoginFormBuilder loginFormBuilder = context.getBean(LoginFormBuilder.class);
        final Node login = loginFormBuilder.getLoginForm();
        Assert.assertNotNull(login);

        final Scene scene = new Scene((Parent) login);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void loginTest() throws JSONException {

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

    /*@Test
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
    }*/

}
