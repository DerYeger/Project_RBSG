package de.uniks.se19.team_g.project_rbsg.Ingame;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.springframework.stereotype.Controller;

import java.awt.*;

@Controller
public class IngameViewController {

    @FXML
    StackPane playgroundPane;

    @FXML
    Pane player1Pane;

    @FXML
    Pane player2Pane;

    @FXML
    Pane player3Pane;

    @FXML
    Pane player4Pane;

    @FXML
    Pane statusPane;

    @FXML
    Pane bottomLinePane;

    @FXML
    Pane chatLogPane;

    @FXML
    Pane leaveGamePane;

    private StatusBar statusBar;
    private LeaveGame leaveGame;

    public void init() {
        statusBar = new StatusBar();
        statusPane.getChildren().add(statusBar.buildStatusBar());
        leaveGame = new LeaveGame();
        leaveGamePane.getChildren().add(leaveGame.buildleaveGame());
    }

}
