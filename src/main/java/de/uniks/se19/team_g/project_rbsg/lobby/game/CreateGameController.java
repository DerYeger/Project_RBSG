package de.uniks.se19.team_g.project_rbsg.lobby.game;

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
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

/**
 * @author Juri Lozowoj
 * @edited Georg Siebert
 * @author Jan Müller
 */
@Controller
public class CreateGameController implements Rincled
{
    //TODO use correct icons
    private URL createBlack = getClass().getResource("/assets/icons/navigation/checkBlack.png");
    private URL createWhite = getClass().getResource("/assets/icons/navigation/checkWhite.png");
    private URL cancelBlack = getClass().getResource("/assets/icons/navigation/crossBlack.png");
    private URL cancelWhite = getClass().getResource("/assets/icons/navigation/crossWhite.png");

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

    private GameCreator gameCreator;
    private JoinGameManager joinGameManager;
    private Game game;

    private Node root;
    private UserProvider userProvider;

    private final int NUMBER_OF_PLAYERS_TWO = 2;
    private final int NUMBER_OF_PLAYERS_FOUR = 4;

    private int numberOfPlayers = NUMBER_OF_PLAYERS_TWO;

    private final GameProvider gameProvider;

    @Autowired
    public CreateGameController(@Nullable GameCreator gameCreator, @Nullable JoinGameManager joinGameManager, @Nullable GameProvider gameProvider, @NonNull UserProvider userProvider) {
        this.gameCreator = gameCreator;
        this.joinGameManager = joinGameManager;
        this.gameProvider = gameProvider;
        this.userProvider = userProvider;
    }

    public void init(){
        cancel.setOnAction(this::closeCreateGameWindow);
        create.setOnAction(this::createGame);
        create.setDefaultButton(true);

        JavaFXUtils.setButtonIcons(create, createWhite, createBlack, 40);
        JavaFXUtils.setButtonIcons(cancel, cancelWhite, cancelBlack, 40);

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

    public void createGame(@NonNull final ActionEvent event) {
        if(this.gameName.getText() != null && (!this.gameName.getText().equals("")) && this.numberOfPlayers != 0){
            this.game = new Game(gameName.getText(), this.numberOfPlayers);
            @SuppressWarnings("unchecked")
            final CompletableFuture<HashMap<String, Object>> gameRequestAnswerPromise = this.gameCreator.sendGameRequest(this.userProvider.get(), game);
            gameRequestAnswerPromise
                    .thenAccept(map -> Platform.runLater(() -> onGameRequestReturned(map)))
                    .exceptionally(exception -> {
                        handleGameRequestErrors("Fehler", "Fehler: Keine Verbindung zum Server moeglich", exception.getMessage());
                        return null;
                    });
            closeCreateGameWindow(null);
        } else if((this.gameName.getText() == null) || this.gameName.getText().equals("")){
            handleGameRequestErrors("Fehler", "Fehler: Fehler bei Eingabeinformation", "Fehler: Fehler bei Eingabeinformation");
        }
    }

    private void onGameRequestReturned(@Nullable HashMap<String, Object> answer) {
        final String gameId;
        if (answer != null){
            if(answer.get("status").equals("succes")){
                @SuppressWarnings("unchecked")
                final HashMap<String, Object> data = (HashMap<String, Object>) answer.get("data");
                gameId = (String) data.get("gameId");
                this.game.setId(gameId);
                this.joinGameManager.joinGame(userProvider.get(), game);
                gameProvider.set(game);
            } else if (answer.get("status").equals("failure")){
                handleGameRequestErrors((String)answer.get("status"), "Fehler beim Erstellen des Spiels", (String)answer.get("message"));

            }
        }
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

    public void handleGameRequestErrors(String title, String headerText, String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }


}
