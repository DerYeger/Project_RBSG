package de.uniks.se19.team_g.project_rbsg.controller;

import de.uniks.se19.team_g.project_rbsg.apis.LoginManager;
import de.uniks.se19.team_g.project_rbsg.apis.RegistrationManager;
import de.uniks.se19.team_g.project_rbsg.model.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.lang.Nullable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

/**
 * @author Jan Müller
 * @edited Juri Lozowoj
 * @edited Keanu Stückrad
 */
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

    private  User user;
    private final LoginManager loginManager;
    private final RegistrationManager registrationManager;

    public LoginFormController(@NonNull final LoginManager loginManager, @NonNull final RegistrationManager registrationManager) {
        this.loginManager = loginManager;
        this.registrationManager = registrationManager;
    }

    public void init() {
        addEventListeners();
    }

    private void addEventListeners() {
        loginButton.setOnAction(this::loginAction);
        registerButton.setOnAction(this::registerAction);
    }

    private void loginAction(@NotNull final ActionEvent event){
        if (nameField.getText() != null && passwordField != null) {
            user = new User(nameField.getText(), passwordField.getText());
            final CompletableFuture<HashMap<String, Object>> answerPromise = loginManager.onLogin(user);
            answerPromise.thenAccept(
                    map -> Platform.runLater(() -> onLoginReturned(map))
            );
        }
    }

    public void onLoginReturned(@Nullable final HashMap<String, Object> answer) {
        if (answer != null) {
            final String status = (String) answer.get("status");
            final String message = (String) answer.get("message");
            final HashMap<String, Object> data = (HashMap<String, Object>) answer.get("data");
            final String userKey = (String) data.get("userKey");
            if (status.equals("success")){
                newScene(new User(user, userKey));
            } else if(status.equals("failure")) {
                invalidCredentialsAlert(status,  message, "Login");
            }
        } else {
            noConnectionAlert();
        }
    }

    private void registerAction(@NotNull final ActionEvent event) {
        if (nameField.getText() != null && passwordField.getText() != null) {
            user = new User(nameField.getText(), passwordField.getText());
            final CompletableFuture<HashMap<String, Object>> answerPromise = registrationManager.onRegistration(user);
            answerPromise.thenAccept(
              map -> Platform.runLater(() -> onRegistrationReturned(map))
            );
        }
    }

    private void onRegistrationReturned(@Nullable final HashMap<String, Object> answer) {
        if (answer != null) {
            final String status = (String) answer.get("status");
            final String message = (String) answer.get("message");
            if (answer.get("status").equals("success")){
                // do login
                loginAction(new ActionEvent());
            } else if(answer.get("status").equals("failure")) {
                invalidCredentialsAlert(status, message, "Registration");
            }
        } else {
            noConnectionAlert();
       }
    }

    public void newScene(@NonNull final User user) {
        // change scene here...
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Platzhalter");
        alert.setHeaderText("Login erfolgreich");
        alert.setContentText("Szenenwechsel zur Lobby muss noch implementiert werden");
        alert.showAndWait();
    }

    public static void noConnectionAlert() {
        // alert failure when there is no server connection
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("failure");
        alert.setHeaderText("Login failed");
        alert.setContentText("No server connection");
        alert.showAndWait();
    }

    public static void invalidCredentialsAlert(@NonNull final String status, @NonNull final String message, @NonNull final String typeOfFail) {
        // alert failure when typing invalid credentials
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(status);
        alert.setHeaderText(typeOfFail + " failed");
        alert.setContentText(message);
        alert.showAndWait();
    }

}
