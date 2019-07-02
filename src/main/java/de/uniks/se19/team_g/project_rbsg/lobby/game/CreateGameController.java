package de.uniks.se19.team_g.project_rbsg.lobby.game;

import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.alert.AlertBuilder;
import de.uniks.se19.team_g.project_rbsg.model.Game;
import de.uniks.se19.team_g.project_rbsg.model.GameProvider;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;

import javax.annotation.Nonnull;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

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
            @Nullable SceneManager sceneManager
    ) {
        this.gameCreator = gameCreator;
        this.joinGameManager = joinGameManager;
        this.gameProvider = gameProvider;
        this.userProvider = userProvider;
        this.alertBuilder = alertBuilder;
        this.sceneManager = sceneManager;
    }

    public void init(){
        cancel.setOnAction(this::closeCreateGameWindow);
        create.setOnAction(this::createGame);
        create.setDefaultButton(true);

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
            this.game = new Game(gameName.getText(), this.numberOfPlayers);
            @SuppressWarnings("unchecked")
            final CompletableFuture<HashMap<String, Object>> gameRequestAnswerPromise = this.gameCreator.sendGameRequest(this.userProvider.get(), game);
            gameRequestAnswerPromise
                    .thenAccept(map -> Platform.runLater(() -> onGameRequestReturned(map)))
                    .exceptionally(exception -> {
                        handleGameRequestErrors(AlertBuilder.Text.NO_CONNECTION);
                        return null;
                    });
        } else if((this.gameName.getText() == null) || this.gameName.getText().equals("")){
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
                            sceneManager.setScene(SceneManager.SceneIdentifier.WAITING_ROOM, false, null);
                        },
                        Platform::runLater
                    );
            } else if (answer.get("status").equals("failure")){
                handleGameRequestErrors(AlertBuilder.Text.CREATE_GAME_ERROR);

            }
        }
        closeCreateGameWindow(null);
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

    public void handleGameRequestErrors(@Nonnull final AlertBuilder.Text text) {
        alertBuilder.information(text);
    }
}
