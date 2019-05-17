package de.uniks.se19.team_g.project_rbsg.apis;

import com.mashape.unirest.http.exceptions.UnirestException;
import de.uniks.se19.team_g.project_rbsg.model.User;
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
import org.springframework.lang.NonNull;
import org.testfx.framework.junit.ApplicationTest;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * @author Keanu Stückrad
 */
public class LoginMangerTest extends ApplicationTest {

    private LoginManager loginManager;

    @Override
    public void start(@NonNull final Stage stage) throws IOException {
        final LoginFormBuilder loginFormBuilder = new LoginFormBuilder();
        final Node login = loginFormBuilder.getLoginForm();
        Assert.assertNotNull(login);

        final Scene scene = new Scene((Parent) login);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void loginTest() throws JSONException {

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

        final Button loginButton = lookup("#login-button").queryButton();
        Assert.assertNotNull(loginButton);

        clickOn(nameInput);
        write("WrongName");

        clickOn(passwordInput);
        write("thisIsNotARightPassword");

        clickOn(loginButton);
    }

}
