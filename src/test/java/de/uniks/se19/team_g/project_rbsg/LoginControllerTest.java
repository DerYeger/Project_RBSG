package de.uniks.se19.team_g.project_rbsg;

import de.uniks.se19.team_g.project_rbsg.apis.RegistrationManager;
import de.uniks.se19.team_g.project_rbsg.controller.LoginFormController;
import de.uniks.se19.team_g.project_rbsg.model.User;
import de.uniks.se19.team_g.project_rbsg.view.LoginFormBuilder;
import de.uniks.se19.team_g.project_rbsg.view.LoginSceneBuilder;
import de.uniks.se19.team_g.project_rbsg.view.SplashImageBuilder;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.testfx.framework.junit.ApplicationTest;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@RunWith(SpringJUnit4ClassRunner .class)
@ContextConfiguration(classes = {JavaConfig.class, LoginFormController.class, FXMLLoader.class, LoginFormBuilder.class, LoginSceneBuilder.class, SplashImageBuilder.class, RegistrationManager.class})
public class LoginControllerTest extends ApplicationTest{

    @Autowired
    private ApplicationContext context;

    public void start(@NonNull Stage stage) throws IOException {
        LoginFormBuilder loginFormBuilder = new LoginFormBuilder(context.getBean(FXMLLoader.class)){

            @Override
            public Node getLoginForm() throws IOException {
                Node loginForm = context.getBean(FXMLLoader.class).load();

                LoginFormController testLoginFormController = new LoginFormController(
                        new RegistrationManager(new RestTemplate()){
                            @Override
                            public CompletableFuture onRegistration(@NonNull User user){
                                throw new RestClientResponseException("Fehler", 404, "Fehler bei der Registrierung", null, null, null);
                            }
                        });
                testLoginFormController.init();
                return loginForm;
            }
        };

        Node testLoginForm = loginFormBuilder.getLoginForm();
        final Scene scene = new Scene((Parent) testLoginForm);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void test(){
        final TextInputControl nameFiled = lookup("#nameFiel").queryTextInputControl();
        final TextInputControl passwordField = lookup("#passwordFiled").queryTextInputControl();
        final Button registerButton = lookup("#registerButton").queryButton();
        Assert.assertNotNull(nameFiled);
        Assert.assertNotNull(nameFiled);

        final String testName = "Tom Waits";
        clickOn(nameFiled);
        write(testName);
        press(KeyCode.ENTER);
        release(KeyCode.ENTER);

        final String password = "geheim";
        clickOn(passwordField);
        write(password);
        press(KeyCode.ENTER);
        release(KeyCode.ENTER);

        clickOn(registerButton);

    }
}

