package de.uniks.se19.team_g.project_rbsg.controller;

import com.mashape.unirest.http.exceptions.UnirestException;
import de.uniks.se19.team_g.project_rbsg.apis.LoginManager;
import de.uniks.se19.team_g.project_rbsg.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;

import javax.validation.constraints.NotNull;
import java.util.concurrent.ExecutionException;

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

    private void loginAction(@NotNull final ActionEvent event){
        try {
            if (nameField.getText() != null && passwordField != null) {
                User user = LoginManager.onLogin(new User(nameField.getText(), passwordField.getText()));
                if (user != null) {
                    onLogin(user);
                }
            }
        } catch (ExecutionException e) {
            LoginManager.noConnectionAlert();
        } catch (InterruptedException e) {
            LoginManager.noConnectionAlert();
        } catch (UnirestException e) {
            LoginManager.noConnectionAlert();
        }
    }

    private void registerAction(@NotNull final ActionEvent event) {

    }

    public void onLogin(@NonNull User user) {
        // change scene here...
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Platzhalter");
        alert.setHeaderText("Login erfolgreich");
        alert.setContentText("Szenenwechsel zur Lobby muss noch implementiert werden");
        alert.showAndWait();
    }

    public void onRegistration() {

    }
}
