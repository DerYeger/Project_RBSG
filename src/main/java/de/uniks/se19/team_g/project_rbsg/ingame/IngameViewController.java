package de.uniks.se19.team_g.project_rbsg.ingame;

import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.ingame.event.GameEventHandlerManager;
import de.uniks.se19.team_g.project_rbsg.lobby.model.Player;
import de.uniks.se19.team_g.project_rbsg.model.GameProvider;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.termination.RootController;
import de.uniks.se19.team_g.project_rbsg.termination.Terminable;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;

/**
 * @author  Keanu Stückrad
 * @author Jan Müller
 */
@Controller
public class IngameViewController implements RootController, Terminable {

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
    /*
    @FXML
    Pane chatLogPane;
    */

    private StatusBarBuilder statusBar;
    private PlayerCardBuilder playerCard;
    private PlayerCardBuilder playerCard2;
    private PlayerCardBuilder playerCard3;
    private PlayerCardBuilder playerCard4;

    private final GameProvider gameProvider;
    private final UserProvider userProvider;
    private final SceneManager sceneManager;
    @NonNull
    private final GameEventHandlerManager gameEventHandlerManager;

    @Autowired
    public IngameViewController(@NonNull final GameProvider gameProvider,
                                @NonNull final UserProvider userProvider,
                                @NonNull final SceneManager sceneManager,
                                @NonNull final GameEventHandlerManager gameEventHandlerManager){
        this.gameProvider = gameProvider;
        this.userProvider = userProvider;
        this.sceneManager = sceneManager;
        this.gameEventHandlerManager = gameEventHandlerManager;
    }

    public void init() {
        initBuilders();
        setNodes();

        gameEventHandlerManager.startSocket(gameProvider.get().getId());
    }

    private void initBuilders() {
        statusBar = new StatusBarBuilder(gameProvider.get().getNeededPlayer() - 1);
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
            player3Pane.getChildren().add(playerCard3.buildPlayerCard());
            player4Pane.getChildren().add(playerCard4.buildPlayerCard());
        }
    }

    @Override
    public void setAsRootController() {
        sceneManager.setRootController(this);
    }

    @Override
    public void terminate() {
        gameEventHandlerManager.terminate();
    }
}
