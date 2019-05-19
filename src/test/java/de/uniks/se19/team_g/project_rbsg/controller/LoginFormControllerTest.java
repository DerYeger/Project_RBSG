package de.uniks.se19.team_g.project_rbsg.controller;


import de.uniks.se19.team_g.project_rbsg.JavaConfig;
import de.uniks.se19.team_g.project_rbsg.apis.LoginManager;
import de.uniks.se19.team_g.project_rbsg.apis.RegistrationManager;
import de.uniks.se19.team_g.project_rbsg.view.LoginFormBuilder;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputControl;
import javafx.stage.Stage;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testfx.framework.junit.ApplicationTest;

import java.io.IOException;

/**
 * @author Keanu Stückrad
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JavaConfig.class, LoginFormBuilder.class, LoginFormController.class, RegistrationManager.class, LoginManager.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LoginFormControllerTest extends ApplicationTest {

    // private LoginManager loginManager;

    @Autowired
    private ApplicationContext context;

    @Override
    public void start(@NonNull final Stage stage) throws IOException {
        final Node loginForm = context.getBean(LoginFormBuilder.class).getLoginForm();
        Assert.assertNotNull(loginForm);

        Scene scene = new Scene((Parent) loginForm);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void loginTestSuccess() {

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
    public void loginTestFailureInvalidCredentialsAlert() {

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
        write("falsePassword");

        clickOn(loginButton);
    }

    @Test
    public void loginTestFailureNoConnection() {

        // loginManager = setLoginManager("failure", "No server connection", Json.createObjectBuilder().build());

        final Button loginButton = lookup("#login-button").queryButton();
        Assert.assertNotNull(loginButton);

        clickOn(loginButton);
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
        write("MasterChief"); // Server has to be simulated

        clickOn(passwordInput);
        write("john-117"); // so that we dont always get new users

        clickOn(registrationButton);

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

        clickOn(passwordInput);
        write("john-117");

        clickOn(registrationButton);
    }

    @Test
    public void registrationTestFailureNoConnection() {

        final Button registrationButton = lookup("#registration-button").queryButton();
        Assert.assertNotNull(registrationButton);

        clickOn(registrationButton);
    }


}

