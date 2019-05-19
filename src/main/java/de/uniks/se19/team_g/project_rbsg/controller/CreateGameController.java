package de.uniks.se19.team_g.project_rbsg.controller;

import de.uniks.se19.team_g.project_rbsg.apis.GameCreator;
import de.uniks.se19.team_g.project_rbsg.model.Game;
import de.uniks.se19.team_g.project_rbsg.model.GameBuilder;
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

    private final GameCreator gameCreator;
    private GameBuilder gameBuilder;
    private User user;
    private int numberOfPlayers;

    private final int NUMBER_OF_PLAYERS_TWO = 2;
    private final int NUMBER_OF_PLAYERS_FOUR = 4;


    public CreateGameController(@Nullable GameCreator gameCreator){
        this.gameCreator = ((gameCreator == null) ? new GameCreator(null) : gameCreator);
        this.gameBuilder = new GameBuilder();
    }

    public void init(){
        twoPlayers.setOnAction(this::setNumberOfPlayersToTwo);
        fourPlayers.setOnAction(this::setNumberOfPlayersToFour);
        cancel.setOnAction(this::closeCreateGameWindow);
        create.setOnAction(this::createGame);
    }

    public void createGame(@NonNull final ActionEvent event){
        if(this.gameName.getText() != null  && this.numberOfPlayers != 0){
            Game newGame = this.gameBuilder.getGame(gameName.getText(), this.numberOfPlayers);
            this.gameCreator.sendGameRequest(this.user, newGame);
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


}
