package de.uniks.se19.team_g.project_rbsg.Ingame;

import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.Contract.DataClasses.Player;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.springframework.stereotype.Controller;

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
    private PlayerCard playerCard;
    private Node leaveGameNode;

    public void init() {
        statusBar = new StatusBar();
        statusPane.getChildren().add(statusBar.buildStatusBar());
        leaveGame = new LeaveGame();
        leaveGameNode = leaveGame.buildLeaveGame();
        leaveGamePane.getChildren().add(leaveGameNode);
        Button leaveGameButton = (Button) leaveGameNode;
        leaveGameButton.setOnAction(event ->
                {}//back to lobby, close WS
        );
        playerCard = new PlayerCard();
        player1Pane.getChildren().add(playerCard.buildPlayerCard(new Player("MasterChief")));
    }

}
