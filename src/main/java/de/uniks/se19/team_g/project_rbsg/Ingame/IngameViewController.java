package de.uniks.se19.team_g.project_rbsg.Ingame;

import de.uniks.se19.team_g.project_rbsg.Lobby.CrossCutting.DataClasses.Player;
import de.uniks.se19.team_g.project_rbsg.model.GameProvider;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.view.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
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
    private PlayerCard playerCard2;
    private PlayerCard playerCard3;
    private PlayerCard playerCard4;
    private Node leaveGameNode;

    private final GameProvider gameProvider;
    private final UserProvider userProvider;
    private final SceneManager sceneManager;

    @Autowired
    public IngameViewController(@NonNull final GameProvider gameProvider, @NonNull final UserProvider userProvider, @NonNull final SceneManager sceneManager){
        this.gameProvider = gameProvider;
        this.userProvider = userProvider;
        this.sceneManager = sceneManager;
    }

    public void init() {
        statusBar = new StatusBar();
        statusPane.getChildren().add(statusBar.buildStatusBar());
        leaveGame = new LeaveGame();
        leaveGameNode = leaveGame.buildLeaveGame();
        leaveGamePane.getChildren().add(leaveGameNode);
        Button leaveGameButton = (Button) leaveGameNode;
        leaveGameButton.setOnAction(event -> {
                sceneManager.setLobbyScene();
                gameProvider.clear();
            }//back to lobby, close WS
        );
        playerCard = new PlayerCard();
        playerCard2 = new PlayerCard();
        playerCard3 = new PlayerCard();
        playerCard4 = new PlayerCard();
        player1Pane.getChildren().add(playerCard.buildPlayerCard());
        player2Pane.getChildren().add(playerCard2.buildPlayerCard());
        player3Pane.getChildren().add(playerCard3.buildPlayerCard());
        player4Pane.getChildren().add(playerCard4.buildPlayerCard());
        playerCard.setPlayer(new Player(userProvider.get().getName()));
    }

}
