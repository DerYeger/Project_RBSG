package de.uniks.se19.team_g.project_rbsg.view;

import de.uniks.se19.team_g.project_rbsg.controller.LoginFormController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;

public class LoginFormBuilder {

    private static Node loginForm;

    public static Node getLoginForm() throws IOException {
        if (loginForm == null) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(LoginFormBuilder.class.getResource("login-form.fxml"));
            LoginFormController loginFormController = fxmlLoader.getController();
            loginForm = fxmlLoader.load();
        }
        return loginForm;
     }
}
