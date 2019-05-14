package de.uniks.se19.team_g.project_rbsg.controller;

import de.uniks.se19.team_g.project_rbsg.apis.RegistrationManager;
import de.uniks.se19.team_g.project_rbsg.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Controller;

import javax.validation.constraints.NotNull;
import java.util.HashMap;

@Controller
public class LoginFormController {

    @FXML
    private TextField nameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    public void init() {
        addEventListeners();
    }

    private void addEventListeners() {
        loginButton.setOnAction(this::loginAction);
        registerButton.setOnAction(this::registerAction);
    }

    private void loginAction(@NotNull final ActionEvent event) {

    }

    private void registerAction(@NotNull final ActionEvent event) {
        User user = null;
        if (nameField.getText() != null && passwordField.getText() != null) {
            user = new User(nameField.getText(), passwordField.getText());
        }
        if (user != null){
           RegistrationManager registrationManager = new RegistrationManager();
           HashMap<String, Object> answer = registrationManager.onRegistration(user);
           if (answer.get("status").equals("success")){
               //loginManager.onLogin();
           } else if(answer.get("status").equals("failure") && answer.get("message").equals("Name already taken")){
               Alert alert = new Alert(Alert.AlertType.ERROR);
               alert.setTitle("Fehler");
               alert.setHeaderText("Fehler bei der Registrierung");
               alert.setContentText("Dieser Benutzer ist bereits registriert");
               alert.showAndWait();
           }
       }
    }

    public void onLogin() {

    }

    public void onRegistration() {

    }
}
