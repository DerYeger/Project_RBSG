package de.uniks.se19.team_g.project_rbsg.controller;

import de.uniks.se19.team_g.project_rbsg.apis.LoginManager;
import de.uniks.se19.team_g.project_rbsg.apis.RegistrationManager;
import de.uniks.se19.team_g.project_rbsg.model.User;
import de.uniks.se19.team_g.project_rbsg.view.SceneManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
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

    @Autowired
    ConfigurableApplicationContext context;

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
            answerPromise
                    .thenAccept(map -> Platform.runLater(() -> onLoginReturned(map)))
                    .exceptionally(exception ->  {
                        handleRequestErrors("failure", exception.getMessage(), "Login");
                        return null;
                    });
        }
    }

    public void onLoginReturned(@Nullable final HashMap<String, Object> answer) {
        if (answer != null) {
            final String status = (String) answer.get("status");
            if (status.equals("success")){
                final HashMap<String, Object> data = (HashMap<String, Object>) answer.get("data");
                final String userKey = "";
                if(data != null){
                    userKey.concat((String) data.get("userKey"));
                    newScene(new User(user, userKey));
                }
            } else if(status.equals("failure")) {
                final String message = (String) answer.get("message");
                handleRequestErrors(status,  message, "Login");
            }
        }
    }

    private void registerAction(@NotNull final ActionEvent event) {
        if (nameField.getText() != null && passwordField.getText() != null) {
            user = new User(nameField.getText(), passwordField.getText());
            final CompletableFuture<HashMap<String, Object>> answerPromise = registrationManager.onRegistration(user);
            answerPromise
                    .thenAccept(map -> Platform.runLater(() -> onRegistrationReturned(map, event)))
                    .exceptionally(exception ->  {
                        handleRequestErrors("failure", exception.getMessage(), "Registration");
                        return null;
                    });
        }
    }

    private void onRegistrationReturned(@Nullable final HashMap<String, Object> answer, ActionEvent event) {
        if (answer != null) {
            final String status = (String) answer.get("status");
            if (answer.get("status").equals("success")){
                this.loginAction(event);
            } else if(answer.get("status").equals("failure")) {
                final String message = (String) answer.get("message");
                handleRequestErrors(status, message, "Registration");
            }
        }
    }

    public void newScene(@NonNull final User user) {
        context.getBean(SceneManager.class).setLobbyScene();
    }

    public static void handleRequestErrors(@NonNull final String status, @NonNull final String message, @NonNull final String typeOfFail) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(status);
        alert.setHeaderText(typeOfFail + " failed");
        alert.setContentText(message);
        alert.showAndWait();
    }

}