package de.uniks.se19.team_g.project_rbsg.controller;

import com.mashape.unirest.http.exceptions.UnirestException;
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
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.Nullable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
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

    private final RegistrationManager registrationManager;

    @Autowired
    private ApplicationContext context;

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
        User user = null;
        if (nameField.getText() != null && passwordField.getText() != null) {
            user = new User(nameField.getText(), passwordField.getText());
        }
        if (user != null){
            final CompletableFuture<HashMap<String, Object>> answerPromise = registrationManager.onRegistration(user);
            answerPromise.thenAccept(
              map -> Platform.runLater(() -> onRegistrationReturned(map))
            );
        }
    }

    private void onRegistrationReturned(@Nullable HashMap<String, Object> answer) {
        final String messageFromServer;
        if (answer != null) {
            messageFromServer = (String) answer.get("status");
            if (answer.get("status").equals("success")){
                //loginManager.onLogin();
            } else if(answer.get("status").equals("failure") && answer.get("message").equals("Name already taken")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Fehler");
                alert.setHeaderText("Fehler bei der Registrierung");
                alert.setContentText(messageFromServer);
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fehler");
            alert.setHeaderText("Fehler bei der Registrierung");
            alert.setContentText("Server fuer die Registrierung antwortet nicht");
            alert.showAndWait();
       }
    }

    public void onLogin(@NonNull User user) {
        context.getBean(SceneManager.class).setLobbyScene();
    }

    public void onRegistration() {

    }
}
