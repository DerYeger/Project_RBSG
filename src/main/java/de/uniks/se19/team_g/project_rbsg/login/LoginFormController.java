package de.uniks.se19.team_g.project_rbsg.login;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.configuration.ApplicationStateInitializer;
import de.uniks.se19.team_g.project_rbsg.model.User;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.server.rest.LoginManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.RegistrationManager;
import de.uniks.se19.team_g.project_rbsg.server.websocket.WebSocketConfigurator;
import io.rincl.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.HttpClientErrorException;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author Jan Müller
 * @author Juri Lozowoj
 * @author Keanu Stückrad
 * @author  Georg Siebert
 */
@Controller
@Scope("prototype")
public class LoginFormController implements Rincled
{

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @FXML
    private TextField nameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    @FXML
    private StackPane errorMessageBox;

    @FXML
    private Label errorMessage;

    @FXML
    private Label progressLabel;

    private Node loading;
    private LoadingIndicatorCardBuilder loadingIndicatorCardBuilder;
    public SimpleBooleanProperty loadingFlag;
    private SimpleBooleanProperty errorFlag;
    private User user;

    @Nonnull
    private final LoginManager loginManager;
    @Nonnull
    private final RegistrationManager registrationManager;
    @Nonnull
    private final SceneManager sceneManager;
    @Nonnull
    private final ApplicationStateInitializer appStateInitializer;
    @Nonnull
    private final UserProvider userProvider;

    @Autowired
    public LoginFormController(
            @Nonnull final UserProvider userProvider,
            @Nonnull final LoginManager loginManager,
            @Nonnull final RegistrationManager registrationManager,
            @Nonnull final SceneManager sceneManager,
            @Nonnull final ApplicationStateInitializer appStateInitializer
        ) {
        this.userProvider = userProvider;
        this.loginManager = loginManager;
        this.registrationManager = registrationManager;
        this.sceneManager = sceneManager;
        this.appStateInitializer = appStateInitializer;
    }

    public void init() {
        addEventListeners();
        addLoadingIndicator();
        addErrorFlag();
        loginButton.setDefaultButton(true);
        Tooltip tooltip = new Tooltip("ENTER");
        tooltip.setStyle("-fx-font-size: 14;");
        loginButton.setTooltip(tooltip);
        updateLabels();

        final String rbsg_user = System.getenv("RBSG_USER");
        final String rbsg_password = System.getenv("RBSG_PW");
        if (rbsg_user != null) {
            nameField.setText(rbsg_user);
        }
        if (rbsg_password != null) {
            passwordField.setText(rbsg_password);
        }
    }

    private void updateLabels()
    {
        loginButton.setText(getResources().getString("loginButton"));
        registerButton.setText(getResources().getString("registerButton"));
        nameField.setPromptText(getResources().getString("name_promptText"));
        passwordField.setPromptText(getResources().getString("password_promptText"));
    }

    private void addErrorFlag() {
        if(errorFlag == null){
            errorFlag = new SimpleBooleanProperty(false);
        }
        errorMessage.visibleProperty().bind(errorFlag);
    }

    private void addLoadingIndicator() {
        loadingIndicatorCardBuilder = new LoadingIndicatorCardBuilder();
        loading = loadingIndicatorCardBuilder.buildProgressIndicatorCard();
        errorMessageBox.getChildren().add(loading);
        if(loadingFlag == null){
            loadingFlag = new SimpleBooleanProperty(false);
        }
        loading.visibleProperty().bind(loadingFlag);
        nameField.disableProperty().bind(loadingFlag);
        passwordField.disableProperty().bind(loadingFlag);
        loginButton.disableProperty().bind(loadingFlag);
        registerButton.disableProperty().bind(loadingFlag);
    }

    private void addEventListeners() {
        loginButton.setOnAction(event -> loginAction());
        registerButton.setOnAction(event -> registerAction());
    }

    private void registerAction() {
        initFlags();
        if (nameField.getText() != null && passwordField.getText() != null) {
            user = new User(nameField.getText(), passwordField.getText());
            final CompletableFuture<ResponseEntity<ObjectNode>> answerPromise = registrationManager.onRegistration(user);
            answerPromise.thenAccept(this::onRegistrationReturned)
                    .exceptionally(this::handleException);
        }
    }

    private void loginAction() {
        initFlags();
        if (nameField.getText() != null && passwordField.getText() != null) {
            user = new User(nameField.getText(), passwordField.getText());
            final CompletableFuture<ResponseEntity<ObjectNode>> answerPromise = loginManager.onLogin(user);
            answerPromise.thenAccept(this::onLoginReturned)
                    .exceptionally(this::handleException);
        }
    }

    private void onRegistrationReturned(@Nullable final ResponseEntity<ObjectNode> answer) {
        if (answer != null && answer.getBody() != null) {

            final ObjectNode body = answer.getBody();

            if (body.get("status").asText().equals("success")){
                this.loginAction();
            } else {
                handleErrorMessage(body.get("message").asText());
            }
        }
    }

    private void onLoginReturned(@Nullable final ResponseEntity<ObjectNode> answer) {
        if (answer != null && answer.getBody() != null) {

            final ObjectNode body = answer.getBody();

            if (body.get("status").asText().equals("success")) {
                final JsonNode data = body.get("data");

                if (data == null) {
                    handleErrorMessage("Empty server response");
                    return;
                }

                onLogin(new User(user, data.get("userKey").asText()));
                user = null;
            } else {
                handleErrorMessage(body.get("message").asText());
            }
        }
    }

    private void onLogin(@NonNull final User user) {
        userProvider.set(user);
        WebSocketConfigurator.userKey = userProvider.get().getUserKey();

        try {
            appStateInitializer.initialize().get();
        } catch (InterruptedException | ExecutionException e) {
            logger.debug("unexpected initializer error", e);
            handleErrorMessage(getResources().getString("unexpectedInitializerError"));
        }
        Platform.runLater(() -> sceneManager.setScene(SceneManager.SceneIdentifier.LOBBY, false, null));
    }

    private void setErrorFlag(boolean flag) {
        errorFlag.set(flag);
    }

    private void setLoadingFlag(boolean flag) {
        loadingFlag.set(flag);
    }

    private void initFlags() {
        setLoadingFlag(true);
        setErrorFlag(false);
    }

    private Void handleException(@NonNull final Throwable throwable) {
        //init with fallback case
        String debugMessage = "Error is not an instance of HttpClientErrorException";
        String errorMessage = "An unexpected error occurred";

        if (throwable.getCause() != null && throwable.getCause() instanceof HttpClientErrorException) {
            try {
                final ObjectNode node = new ObjectMapper().readValue(((HttpClientErrorException) throwable.getCause()).getResponseBodyAsString(), ObjectNode.class);

                if (node.has("status") && node.get("status").asText().equals("failure") && node.has("message")) {
                    debugMessage = null;
                    errorMessage = node.get("message").asText();
                } else {
                    debugMessage = "Unknown exception format";
                    errorMessage = "Unknown server response";
                }
            } catch (IOException e) {
                debugMessage = "Unable to parse exception";
                e.printStackTrace();
            }
        }

        if (debugMessage != null) logger.debug(debugMessage);

        handleErrorMessage(errorMessage);

        return null;
    }

    private void handleErrorMessage(@NonNull final String errorMessage) {
        logger.debug("Error: " + errorMessage);
        setLoadingFlag(false);
        setErrorFlag(true);
        Platform.runLater(() -> this.errorMessage.setText(errorMessage));
    }
}