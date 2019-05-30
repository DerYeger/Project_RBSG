package de.uniks.se19.team_g.project_rbsg.login;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Jan Müller
 * @edited: Juri Lozowoj
 */
@Component
public class LoginFormBuilder {

    private Node loginForm;

    private FXMLLoader fxmlLoader;

    @Autowired
    public LoginFormBuilder(FXMLLoader fxmlLoader) {
        this.fxmlLoader = fxmlLoader;
    }

    public Node getLoginForm() throws IOException {
        if (loginForm == null) {
            fxmlLoader.setLocation(LoginFormBuilder.class.getResource("login-form.fxml"));
            loginForm = fxmlLoader.load();
            final LoginFormController loginFormController = fxmlLoader.getController();
            loginFormController.init();
        }
        return loginForm;
    }

}
