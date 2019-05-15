package de.uniks.se19.team_g.project_rbsg.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Controller;

import javax.validation.constraints.NotNull;

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

    private final RegistrationManager registrationManager;

    public LoginFormController(RegistrationManager registrationManager) {
        this.registrationManager = registrationManager;
    }

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
            HashMap<String, Object> answer = registrationManager.onRegistration(user);
           String messageFromServer = (String) answer.get("message");
           if (answer.get("status").equals("success")){
               //loginManager.onLogin();
           } else if(answer.get("status").equals("failure") && answer.get("message").equals("Name already taken")){
               Alert alert = new Alert(Alert.AlertType.ERROR);
               alert.setTitle("Fehler");
               alert.setHeaderText("Fehler bei der Registrierung");
               alert.setContentText(messageFromServer);
               alert.showAndWait();
           }
       }
    }

    public void onLogin() {

    }

    public void onRegistration() {

    }
}
