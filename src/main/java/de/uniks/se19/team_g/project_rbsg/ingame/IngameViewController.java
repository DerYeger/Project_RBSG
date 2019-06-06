package de.uniks.se19.team_g.project_rbsg.ingame;

import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.lobby.model.Player;
import de.uniks.se19.team_g.project_rbsg.model.GameProvider;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;

/**
 * @author  Keanu Stückrad
 */
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
    Pane leaveGamePane;
    /*
    @FXML
    Pane chatLogPane;
    */

    private StatusBarBuilder statusBar;
    private LeaveGameButtonBuilder leaveGameButton;
    private PlayerCardBuilder playerCard;
    private PlayerCardBuilder playerCard2;
    private PlayerCardBuilder playerCard3;
    private PlayerCardBuilder playerCard4;

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
        initBuilders();
        setNodes();
        initLeaveGameButton();
    }

    private void initBuilders() {
        statusBar = new StatusBarBuilder(gameProvider.get().getNeededPlayer() - 1);
        leaveGameButton = new LeaveGameButtonBuilder();
        playerCard = new PlayerCardBuilder();
        playerCard2 = new PlayerCardBuilder();
        if(gameProvider.get().getNeededPlayer() == 4) {
            playerCard3 = new PlayerCardBuilder();
            playerCard4 = new PlayerCardBuilder();
        }
    }

    private void setNodes() {
        statusPane.getChildren().add(statusBar.buildStatusBar());
        player1Pane.getChildren().add(playerCard.setPlayer(new Player(userProvider.get().getName())));
        player2Pane.getChildren().add(playerCard2.buildPlayerCard());
        if(gameProvider.get().getNeededPlayer() == 4) {
            // if visibility was disabled before for example when leaving game
            player3Pane.setVisible(true);
            player4Pane.setVisible(true);
            player3Pane.getChildren().add(playerCard3.buildPlayerCard());
            player4Pane.getChildren().add(playerCard4.buildPlayerCard());
        } else {
            player3Pane.setVisible(false);
            player4Pane.setVisible(false);
        }
    }

    private void initLeaveGameButton() {
        Node leaveGameNode = leaveGameButton.buildLeaveGame();
        leaveGamePane.getChildren().add(leaveGameNode);
        Button leaveGameButton = (Button) leaveGameNode;
        leaveGameButton.setOnAction(this::leaveGameAction);
    }

    private void leaveGameAction(ActionEvent actionEvent) { //back to lobby, close WS
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Leave Game");
        alert.setHeaderText("Are you sure you want to exit?");
        alert.showAndWait();
        if (alert.getResult().equals(ButtonType.OK)) {
            // WebSocketConfigurator.userKey = userProvider.get().getUserKey();
            sceneManager.setLobbyScene();
            gameProvider.clear();
        } else {
            actionEvent.consume();
        }
    }
}
