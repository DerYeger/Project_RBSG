package de.uniks.se19.team_g.project_rbsg.controller;

import de.uniks.se19.team_g.project_rbsg.apis.GameManager;
import de.uniks.se19.team_g.project_rbsg.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;

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

    private final GameManager gameManager;
    private User user;

    private final int NUMBER_OF_PLAYERS_TWO = 2;
    private final int NUMBER_OF_PLAYERS_FOUR = 4;
    private int numberOfPlayers;


    public CreateGameController(@Nullable GameManager gameManager, @NonNull User user){
        this.gameManager = ((gameManager == null) ? new GameManager(null) : gameManager);
        this.user = user;
    }

    public void initialize(){
        twoPlayers.setOnAction(this::setNumberOfPlayersToTwo);
        fourPlayers.setOnAction(this::setNumberOfPlayersToFour);
        cancel.setOnAction(this::closeCreateGameWindow);
        create.setOnAction((event) -> createGame(event, this.user));
    }

    public void createGame(@NonNull final ActionEvent event, @NonNull User user){
        gameManager.sendGameRequest(user, this.numberOfPlayers);
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


}
