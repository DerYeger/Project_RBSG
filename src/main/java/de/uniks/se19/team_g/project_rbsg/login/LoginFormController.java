package de.uniks.se19.team_g.project_rbsg.login;


import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.model.User;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.server.rest.LoginManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.RegistrationManager;
import de.uniks.se19.team_g.project_rbsg.server.websocket.WebSocketConfigurator;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
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

    @FXML
    private HBox errorMessageBox;

    @FXML
    private Label errorMessage;

    private  User user;
    private final LoginManager loginManager;
    private final RegistrationManager registrationManager;
    private final SceneManager sceneManager;
    private final UserProvider userProvider;

    @Autowired
    public LoginFormController(@NonNull final UserProvider userProvider, @NonNull final LoginManager loginManager, @NonNull final RegistrationManager registrationManager, @NonNull final SceneManager sceneManager) {
        this.userProvider = userProvider;
        this.loginManager = loginManager;
        this.registrationManager = registrationManager;
        this.sceneManager = sceneManager;
    }

    public void init() {
        addEventListeners();
        errorMessageBox.setVisible(false);
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
                        Platform.runLater(() -> this.errorMessageBox.setVisible(true));
                        Platform.runLater(() -> this.errorMessage.setText(exception.getMessage()));
                        return null;
                    });
        }
    }

    public void onLoginReturned(@Nullable final HashMap<String, Object> answer) {
        if (answer != null) {
            final String status = (String) answer.get("status");
            if (status.equals("success")){
                final HashMap<String, Object> data = (HashMap<String, Object>) answer.get("data");
                String userKey = "";
                if(data != null){
                    userKey = ((String) data.get("userKey"));
                }
                onLogin(new User(user, userKey));
            } else if(status.equals("failure")) {
                final String message = (String) answer.get("message");
                Platform.runLater(() -> this.errorMessageBox.setVisible(true));
                Platform.runLater(() -> this.errorMessage.setText(message));
                System.out.println("message: " + message);
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
                        Platform.runLater(() -> this.errorMessageBox.setVisible(true));
                        Platform.runLater(() -> this.errorMessage.setText(exception.getMessage()));
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
                Platform.runLater(() -> this.errorMessageBox.setVisible(true));
                Platform.runLater(() -> this.errorMessage.setText(message));
                System.out.println("message: " + message);
            }
        }
    }

    public void onLogin(@NonNull User user) {
        userProvider.set(user);
        WebSocketConfigurator.userKey = userProvider.get().getUserKey();
        sceneManager.setLobbyScene();
    }
}