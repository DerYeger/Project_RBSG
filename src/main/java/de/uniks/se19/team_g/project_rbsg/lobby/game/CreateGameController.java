package de.uniks.se19.team_g.project_rbsg.lobby.game;

import de.uniks.se19.team_g.project_rbsg.model.Game;
import de.uniks.se19.team_g.project_rbsg.model.GameProvider;
import de.uniks.se19.team_g.project_rbsg.server.rest.JoinGameManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.GameCreator;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

/**
 * @author Juri Lozowoj
 */
@Controller
public class CreateGameController {

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
    public CreateGameController(@Nullable GameCreator gameCreator, @Nullable JoinGameManager joinGameManager, @Nullable GameProvider gameProvider, @NonNull UserProvider userProvider){
        this.gameCreator = gameCreator;
        this.joinGameManager = joinGameManager;
        this.gameProvider = gameProvider;
        this.userProvider = userProvider;
    }

    public void init(){
        cancel.setOnAction(this::closeCreateGameWindow);
        create.setOnAction(this::createGame);

        this.twoPlayers.selectedProperty().addListener(event -> setTwoPlayerGame(event));
        this.fourPlayers.selectedProperty().addListener(event -> setFourPlayerGame(event));
    }


    public void setRootNode(Node root){
        this.root = root;
    }

    public Node getRoot(){
        return this.root;
    }

    public void createGame(@NonNull final ActionEvent event){
        if(this.gameName.getText() != null && (!this.gameName.getText().equals("")) && this.numberOfPlayers != 0){
            this.game = new Game(gameName.getText(), this.numberOfPlayers);
            final CompletableFuture<HashMap<String, Object>> gameRequestAnswerPromise = this.gameCreator.sendGameRequest(this.userProvider.get(), game);
            gameRequestAnswerPromise
                    .thenAccept(map -> Platform.runLater(() -> onGameRequestReturned(map)))
                    .exceptionally(exception -> {
                        handleGameRequestErrors("Fehler", "Fehler: Keine Verbindung zum Server moeglich", exception.getMessage());
                        return null;
                    });
        } else if((this.gameName.getText() == null) || this.gameName.getText().equals("")){
            handleGameRequestErrors("Fehler", "Fehler: Fehler bei Eingabeinformation", "Fehler: Fehler bei Eingabeinformation");
        }
    }

    private void onGameRequestReturned(@Nullable HashMap<String, Object> answer) {
        final String gameId;
        if (answer != null){
            if(answer.get("status").equals("succes")){
                HashMap<String, Object> data = (HashMap<String, Object>) answer.get("data");
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

    public void closeCreateGameWindow(@NonNull final ActionEvent event){
        if (root != null){
            root.setVisible(false);
        }
    }

    public void handleGameRequestErrors(String title, String headerText, String errorMessage){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }


}
