package de.uniks.se19.team_g.project_rbsg.lobby.game;

import de.uniks.se19.team_g.project_rbsg.scene.ExceptionHandler;
import de.uniks.se19.team_g.project_rbsg.scene.SceneConfiguration;
import de.uniks.se19.team_g.project_rbsg.scene.SceneManager;
import de.uniks.se19.team_g.project_rbsg.lobby.core.ui.LobbyViewController;
import de.uniks.se19.team_g.project_rbsg.lobby.loading_screen.LoadingScreenFormBuilder;
import de.uniks.se19.team_g.project_rbsg.overlay.alert.AlertBuilder;
import de.uniks.se19.team_g.project_rbsg.model.Game;
import de.uniks.se19.team_g.project_rbsg.model.GameProvider;
import de.uniks.se19.team_g.project_rbsg.scene.WebSocketExceptionHandler;
import de.uniks.se19.team_g.project_rbsg.server.rest.JoinGameManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.GameCreator;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.util.JavaFXUtils;
import io.rincl.*;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static de.uniks.se19.team_g.project_rbsg.scene.SceneManager.SceneIdentifier.*;

/**
 * @author Juri Lozowoj
 * @edited Georg Siebert
 * @author Jan Müller
 */
@Controller
public class CreateGameController implements Rincled
{
    private static final URL CONFIRM_WHITE = CreateGameController.class.getResource("/assets/icons/navigation/checkWhite.png");
    private static final URL CONFIRM_BLACK = CreateGameController.class.getResource("/assets/icons/navigation/checkBlack.png");
    private static final URL CANCEL_WHITE = CreateGameController.class.getResource("/assets/icons/navigation/crossWhite.png");
    private static final URL CANCEL_BLACK = CreateGameController.class.getResource("/assets/icons/navigation/crossBlack.png");

    private final ExceptionHandler exceptionHandler;

    public Label titleLabel;

    @FXML
    private TextField gameName;

    @FXML
    public ToggleButton twoPlayers;

    @FXML
    public ToggleButton fourPlayers;

    @FXML
    private Button create;

    @FXML
    private Button cancel;

    @FXML
    private ToggleGroup number;

    private Game game;

    private Node root;

    private LobbyViewController lobbyViewController;
    private GridPane loadingScreenForm;
    public LoadingScreenFormBuilder loadingScreenFormBuilder;

    @Nonnull
    private UserProvider userProvider;
    @Nonnull
    private final AlertBuilder alertBuilder;
    @Nonnull
    private GameCreator gameCreator;
    @Nullable
    private final SceneManager sceneManager;
    @Nullable
    private final GameProvider gameProvider;
    @Nullable
    private JoinGameManager joinGameManager;

    private final int NUMBER_OF_PLAYERS_TWO = 2;
    private final int NUMBER_OF_PLAYERS_FOUR = 4;

    private int numberOfPlayers = NUMBER_OF_PLAYERS_TWO;


    @Autowired
    public CreateGameController(
            @Nonnull UserProvider userProvider,
            @Nonnull AlertBuilder alertBuilder,
            @Nonnull GameCreator gameCreator,
            @Nullable JoinGameManager joinGameManager,
            @Nullable GameProvider gameProvider,
            @Nullable SceneManager sceneManager,
            @NonNull LoadingScreenFormBuilder loadingScreenFormBuilder
    ) {
        this.gameCreator = gameCreator;
        this.joinGameManager = joinGameManager;
        this.gameProvider = gameProvider;
        this.userProvider = userProvider;
        this.alertBuilder = alertBuilder;
        this.sceneManager = sceneManager;
        this.loadingScreenFormBuilder = loadingScreenFormBuilder;

        exceptionHandler = new WebSocketExceptionHandler(alertBuilder)
                .onRetry(this::toIngame)
                .onCancel(() -> sceneManager.setScene(SceneConfiguration.of(LOGIN)));
    }

    public void init(){
        cancel.setOnAction(this::closeCreateGameWindow);
        create.setOnAction(this::createGame);
        create.setDefaultButton(true);
        create.setTooltip(new Tooltip("ENTER"));

        JavaFXUtils.setButtonIcons(create, CONFIRM_WHITE, CONFIRM_BLACK, 40);
        JavaFXUtils.setButtonIcons(cancel, CANCEL_WHITE, CANCEL_BLACK, 40);

        this.twoPlayers.selectedProperty().addListener(this::setTwoPlayerGame);
        this.fourPlayers.selectedProperty().addListener(this::setFourPlayerGame);

        number.selectedToggleProperty().addListener((event, oldValue, newValue) -> {
            if (newValue == null){
                oldValue.setSelected(true);
            }
        });

        updateLabels();
    }

    public void updateLabels()
    {
        titleLabel.textProperty().setValue(getResources().getString("title"));
        gameName.setPromptText(getResources().getString("gameName_promptText"));
        twoPlayers.textProperty().setValue(getResources().getString("twoPlayersButton"));
        fourPlayers.textProperty().setValue(getResources().getString("fourPlayersButton"));
    }

    public void setRootNode(Node root){
        this.root = root;
    }

    public Node getRoot(){
        return this.root;
    }

    public void createGame(@Nonnull final ActionEvent event) {
        if(
            this.gameName.getText() != null
            && (!this.gameName.getText().equals(""))
            && this.numberOfPlayers != 0
        ){
            showLoadingScreen();
            this.game = new Game(gameName.getText(), this.numberOfPlayers);
            this.game.setCreator(userProvider.get());

            @SuppressWarnings("unchecked")
            final CompletableFuture<HashMap<String, Object>> gameRequestAnswerPromise = this.gameCreator.sendGameRequest(this.userProvider.get(), game);
            gameRequestAnswerPromise
                    .thenAccept(map -> Platform.runLater(() -> onGameRequestReturned(map)))
                    .exceptionally(exception -> {
                        closeLoadingScreen();
                        handleGameRequestErrors(AlertBuilder.Text.NO_CONNECTION);
                        return null;
                    });

        } else if((this.gameName.getText() == null) || this.gameName.getText().equals("")){
            closeLoadingScreen();
            handleGameRequestErrors(AlertBuilder.Text.INVALID_INPUT);
        }

    }

    public void onGameRequestReturned(@Nullable Map<String, Object> answer) {
        final String gameId;
        if (answer != null && gameProvider != null && sceneManager != null && joinGameManager != null) {
            if(answer.get("status").equals("success")) { //TODO fix autojoin
                @SuppressWarnings("unchecked")
                final Map<String, Object> data = (Map<String, Object>) answer.get("data");
                gameId = (String) data.get("gameId");
                this.game.setId(gameId);
                this.joinGameManager.joinGame(userProvider.get(), game)
                    .thenRunAsync(
                        () -> {
                            gameProvider.set(game);
                            toIngame();
                        },
                        Platform::runLater
                    );
            } else if (answer.get("status").equals("failure")){
                closeLoadingScreen();
                handleGameRequestErrors(AlertBuilder.Text.CREATE_GAME_ERROR);
            }
        }
        closeCreateGameWindow(null);
    }

    private void toIngame() {
        sceneManager
                .setScene(SceneConfiguration
                        .of(INGAME)
                        .withExceptionHandler(exceptionHandler)
                );
    }

    private void setTwoPlayerGame(Observable event) {
        this.numberOfPlayers = NUMBER_OF_PLAYERS_TWO;
    }

    private void setFourPlayerGame(Observable event) {
        this.numberOfPlayers = NUMBER_OF_PLAYERS_FOUR;
    }

    public void closeCreateGameWindow(@Nullable final ActionEvent event) {
        if (root != null) {
            root.setVisible(false);
        }
    }

    private void closeLoadingScreen(){
        if (loadingScreenForm != null){
            loadingScreenForm.setVisible(false);
        }
    }

    public void handleGameRequestErrors(@Nonnull final AlertBuilder.Text text) {
        alertBuilder.information(text);
    }

    private void showLoadingScreen(){
        if(loadingScreenForm == null){
            try{
                this.loadingScreenForm = (GridPane) this.loadingScreenFormBuilder.getLoadingScreenForm();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        if((this.loadingScreenForm != null) && (!this.lobbyViewController.mainStackPane.getChildren().contains(this.loadingScreenForm))){
            loadingScreenForm.setPrefSize(this.lobbyViewController.mainStackPane.getWidth() ,this.lobbyViewController.mainStackPane.getHeight());
            this.lobbyViewController.mainStackPane.getChildren().add(this.loadingScreenForm);
        }
        if ((this.loadingScreenForm != null) && (this.lobbyViewController.mainStackPane.getChildren().contains(this.loadingScreenForm))){
            loadingScreenForm.setVisible(true);
        }
    }

    public LobbyViewController getLobbyViewController() {
        return lobbyViewController;
    }

    public void setLobbyViewController(LobbyViewController lobbyViewController) {
        this.lobbyViewController = lobbyViewController;
    }
}
