package de.uniks.se19.team_g.project_rbsg.waiting_room;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.uniks.se19.team_g.project_rbsg.MusicManager;
import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.chat.ChatController;
import de.uniks.se19.team_g.project_rbsg.chat.ui.ChatBuilder;
import de.uniks.se19.team_g.project_rbsg.configuration.ApplicationState;
import de.uniks.se19.team_g.project_rbsg.login.SplashImageBuilder;
import de.uniks.se19.team_g.project_rbsg.util.JavaFXUtils;
import de.uniks.se19.team_g.project_rbsg.util.Tuple;
import de.uniks.se19.team_g.project_rbsg.waiting_room.event.GameEventHandler;
import de.uniks.se19.team_g.project_rbsg.waiting_room.event.GameEventManager;
import de.uniks.se19.team_g.project_rbsg.lobby.model.Player;
import de.uniks.se19.team_g.project_rbsg.model.GameProvider;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.waiting_room.model.Game;
import de.uniks.se19.team_g.project_rbsg.waiting_room.model.ModelManager;
import javafx.event.ActionEvent;
import de.uniks.se19.team_g.project_rbsg.termination.RootController;
import de.uniks.se19.team_g.project_rbsg.termination.Terminable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;

/**
 * @author  Keanu Stückrad
 * @author Jan Müller
 */
@Scope("prototype")
@Controller
public class WaitingRoomViewController implements RootController, Terminable, GameEventHandler {

    private static final int ICON_SIZE = 40;

    public Pane player1Pane;
    public Pane player2Pane;
    public Pane player3Pane;
    public Pane player4Pane;
    public Pane chatPane; // TODO @DerYeger
    public Pane mapPreviewPane; // TODO @DerYeger
    public Pane miniGamePane; // TODO Tic-Tac-Toe?
    public Pane armyBar; // TODO has to be filled later
    public Button soundButton;
    public Button leaveButton;
    public Button showInfoButton;
    public AnchorPane root;

    private Node chatNode;
    private ChatController chatController;

    private PlayerCardBuilder playerCard;
    private PlayerCardBuilder playerCard2;
    private PlayerCardBuilder playerCard3;
    private PlayerCardBuilder playerCard4;

    private final GameProvider gameProvider;
    private final UserProvider userProvider;
    private final SceneManager sceneManager;
    private final GameEventManager gameEventManager;
    private final MusicManager musicManager;
    private final SplashImageBuilder splashImageBuilder;
    private final ApplicationState applicationState;
    @NonNull
    private final ChatBuilder chatBuilder;
    private final ModelManager modelManager;

    @Autowired
    public WaitingRoomViewController(@NonNull final GameProvider gameProvider,
                                     @NonNull final UserProvider userProvider,
                                     @NonNull final SceneManager sceneManager,
                                     @NonNull final GameEventManager gameEventManager,
                                     @NonNull final MusicManager musicManager,
                                     @NonNull final SplashImageBuilder splashImageBuilder,
                                     @NonNull final ApplicationState applicationState,
                                     @NonNull final ChatBuilder chatBuilder) {
        this.gameProvider = gameProvider;
        this.userProvider = userProvider;
        this.sceneManager = sceneManager;
        this.gameEventManager = gameEventManager;
        this.musicManager = musicManager.init();
        this.splashImageBuilder = splashImageBuilder;
        this.applicationState = applicationState;
        this.chatBuilder = chatBuilder;

        modelManager = new ModelManager();
    }

    public void init() {
        initPlayerCardBuilders();
        setPlayerCardNodes();

        setAsRootController();
        JavaFXUtils.setButtonIcons(
                leaveButton,
                getClass().getResource("/assets/icons/navigation/arrowBackWhite.png"),
                getClass().getResource("/assets/icons/navigation/arrowBackBlack.png"),
                ICON_SIZE
        );
        JavaFXUtils.setButtonIcons(
                showInfoButton,
                getClass().getResource("/assets/icons/navigation/infoWhite.png"),
                getClass().getResource("/assets/icons/navigation/infoBlack.png"),
                ICON_SIZE
        );
        musicManager.initButtonIcons(soundButton);
        root.setBackground(new Background(splashImageBuilder.getSplashImage()));

        initSocket();
    }

    private void initSocket() {
        gameEventManager.addHandler(modelManager);
        gameEventManager.addHandler(this);
        withChatSupport();
        if (applicationState.selectedArmy.get() == null) {
            System.out.println("USER HAS NO ARMY");
            System.out.println("ABORTING GAMESOCKET INIT");
            return;
        }
        gameEventManager.startSocket(gameProvider.get().getId(), applicationState.selectedArmy.get().id.get());
    }

    private void withChatSupport() {
        final Tuple<Node, ChatController> chatComponents = chatBuilder.buildChat(gameEventManager);
        chatNode = chatComponents.first;
        chatController = chatComponents.second;
        chatPane.getChildren().add(chatNode);
    }

    private void initPlayerCardBuilders() {
        playerCard = new PlayerCardBuilder();
        playerCard2 = new PlayerCardBuilder();
        if(gameProvider.get().getNeededPlayer() == 4) {
            playerCard3 = new PlayerCardBuilder();
            playerCard4 = new PlayerCardBuilder();
        }
    }

    private void setPlayerCardNodes() {
        player1Pane.getChildren().add(playerCard.setPlayer(new Player(userProvider.get().getName())));
        player2Pane.getChildren().add(playerCard2.buildPlayerCard());
        playerCard2.switchColumns();
        if(gameProvider.get().getNeededPlayer() == 4) {
            // if visibility was disabled before for example when leaving game
            player3Pane.setVisible(true);
            player4Pane.setVisible(true);
            AnchorPane.setTopAnchor(player1Pane, 110.0);
            AnchorPane.setTopAnchor(player2Pane, 110.0);
            player3Pane.getChildren().add(playerCard3.buildPlayerCard());
            player4Pane.getChildren().add(playerCard4.buildPlayerCard());
            playerCard4.switchColumns();
        } else {
            AnchorPane.setTopAnchor(player1Pane, 180.0);
            AnchorPane.setTopAnchor(player2Pane, 180.0);
            player3Pane.setVisible(false);
            player4Pane.setVisible(false);
        }
    }

    @Override
    public void setAsRootController() {
        sceneManager.setRootController(this);
    }

    @Override
    public void terminate() {
        gameEventManager.terminate();
    }

    public void showInfo(ActionEvent actionEvent) {
        // TODO
    }

    public void leaveRoom(ActionEvent actionEvent) {
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

    public void toggleSound(ActionEvent actionEvent) {
        musicManager.updateMusicButtonIcons(soundButton);
    }

    @Override
    public boolean accepts(@NonNull final ObjectNode message) {
        if (!message.has("action")) return false;

        if (!message.get("action").asText().equals("gameInitFinished")) return false;

        return true;
    }

    @Override
    public void handle(@NonNull final ObjectNode message) {
        final Game game = modelManager.getGame();
        //game SHOULD (no guarantee) be ready now
    }
}
