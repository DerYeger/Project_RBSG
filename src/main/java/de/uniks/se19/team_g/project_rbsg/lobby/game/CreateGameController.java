package de.uniks.se19.team_g.project_rbsg.game;

import de.uniks.se19.team_g.project_rbsg.server.rest.GameCreator;
import de.uniks.se19.team_g.project_rbsg.server.rest.JoinGameManager;
import de.uniks.se19.team_g.project_rbsg.model.Game;
import de.uniks.se19.team_g.project_rbsg.model.GameBuilder;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
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
    public RadioButton twoPlayers;

    @FXML
    public RadioButton fourPlayers;

    @FXML
    private Button create;

    @FXML
    private Button cancel;

    private GameCreator gameCreator;
    private JoinGameManager joinGameManager;
    private Game game;
    private GameBuilder gameBuilder;
    private int numberOfPlayers = 2;
    private Node root;
    private UserProvider userProvider;
    private ToggleGroup toggleGroup;

    private final int NUMBER_OF_PLAYERS_TWO = 2;
    private final int NUMBER_OF_PLAYERS_FOUR = 4;


    public CreateGameController(@Nullable GameCreator gameCreator, @Nullable JoinGameManager joinGameManager, @NonNull UserProvider userProvider){
        this.gameCreator = ((gameCreator == null) ? new GameCreator(null) : gameCreator);
        this.joinGameManager = ((joinGameManager == null) ? new JoinGameManager(null) : joinGameManager);
        this.gameBuilder = new GameBuilder();
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
            this.game = this.gameBuilder.getGame(gameName.getText(), this.numberOfPlayers);
            final CompletableFuture<HashMap<String, Object>> gameRequestAnswerPromise = this.gameCreator.sendGameRequest(this.userProvider.get(), this.game);
            gameRequestAnswerPromise
                    .thenAccept(map -> Platform.runLater(() -> onGameRequestReturned(map)))
                    .exceptionally(exception -> {
                        handleGameRequestErrors("Fehler", "Fehler: Keine Verbindung zum Server moeglich", exception.getMessage());
                        return null;
                    });
        } else if((this.gameName.getText() == null)  || (this.numberOfPlayers == 0) || this.gameName.getText().equals("")){
            handleGameRequestErrors("Fehler", "Fehler: Fehler bei Eingabeinformation", "Fehler: Fehler bei Eingabeinformation");
        }
    }

    private void onGameRequestReturned(@Nullable HashMap<String, Object> answer) {
        final String gameId;
        if (answer != null){
            if(answer.get("status").equals("succes")){
                HashMap<String, Object> data = (HashMap<String, Object>) answer.get("data");
                gameId = (String) data.get("gameId");
                this.game.setGameId(gameId);
                //this.joinGameManager.joinGame(userProvider.get(), game);
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
