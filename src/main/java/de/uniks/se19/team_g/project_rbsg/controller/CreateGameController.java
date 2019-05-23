package de.uniks.se19.team_g.project_rbsg.controller;

import de.uniks.se19.team_g.project_rbsg.apis.GameCreator;
import de.uniks.se19.team_g.project_rbsg.model.Game;
import de.uniks.se19.team_g.project_rbsg.model.GameBuilder;
import de.uniks.se19.team_g.project_rbsg.model.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

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
    private ToggleButton twoPlayers;

    @FXML
    private ToggleButton fourPlayers;

    @FXML
    private Button create;

    @FXML
    private Button cancel;

    private GameCreator gameCreator;
    private GameController gameController;
    private Game game;
    private GameBuilder gameBuilder;
    private User user;
    private int numberOfPlayers;

    private final int NUMBER_OF_PLAYERS_TWO = 2;
    private final int NUMBER_OF_PLAYERS_FOUR = 4;


    public CreateGameController(@Nullable GameCreator gameCreator, @Nullable GameController gameController){
        this.gameCreator = ((gameCreator == null) ? new GameCreator(null) : gameCreator);
        this.gameController = ((gameController == null) ? new GameController(null) : gameController);
        this.gameBuilder = new GameBuilder();
    }

    public void init(){
        twoPlayers.setOnAction(this::setNumberOfPlayersToTwo);
        fourPlayers.setOnAction(this::setNumberOfPlayersToFour);
        cancel.setOnAction(this::closeCreateGameWindow);
        create.setOnAction(this::createGame);
    }

    public void createGame(@NonNull final ActionEvent event){
        if(this.gameName.getText() != null && (!this.gameName.getText().equals("")) && this.numberOfPlayers != 0){
            this.game = this.gameBuilder.getGame(gameName.getText(), this.numberOfPlayers);
            final CompletableFuture<HashMap<String, Object>> gameRequestAnswerPromise = this.gameCreator.sendGameRequest(this.user, this.game);
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
                this.gameController.joinGame(user, game);
            } else if (answer.get("status").equals("failure")){
                handleGameRequestErrors((String)answer.get("status"), "Fehler beim Erstellen des Spiels", (String)answer.get("message"));

            }
        }
    }

    public void setNumberOfPlayersToTwo(@NonNull final ActionEvent event){
        this.numberOfPlayers = NUMBER_OF_PLAYERS_TWO;
    }

    public void setNumberOfPlayersToFour(@NonNull final ActionEvent event){
        this.numberOfPlayers = NUMBER_OF_PLAYERS_FOUR;
    }

    public void closeCreateGameWindow(@NonNull final ActionEvent event){
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    public void handleGameRequestErrors(String title, String headerText, String errorMessage){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }


}
