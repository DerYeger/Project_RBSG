package de.uniks.se19.team_g.project_rbsg.view;

import de.uniks.se19.team_g.project_rbsg.controller.LoginFormController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;

/**
 * @author Jan Müller
 */
public class LoginFormBuilder {

    private Node loginForm;

    public Node getLoginForm() throws IOException {
        if (loginForm == null) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(LoginFormBuilder.class.getResource("login-form.fxml"));
            loginForm = fxmlLoader.load();

            final LoginFormController loginFormController = fxmlLoader.getController();
            loginFormController.init();
        }
        return loginForm;
     }
}
