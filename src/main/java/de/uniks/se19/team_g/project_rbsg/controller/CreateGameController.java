package de.uniks.se19.team_g.project_rbsg.controller;

import de.uniks.se19.team_g.project_rbsg.apis.GameManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
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
    private Button createGame;

    @FXML
    private Button cancelGame;

    private final GameManager gameManager;

    public CreateGameController(@Nullable GameManager gameManager){
        this.gameManager = ((gameManager == null) ? new GameManager(null) : gameManager);
    }

    public void initialize(){
        createGame.setOnAction();
    }


}
