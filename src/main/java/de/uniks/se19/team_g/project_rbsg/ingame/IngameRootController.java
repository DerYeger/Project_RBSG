package de.uniks.se19.team_g.project_rbsg.ingame;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.uniks.se19.team_g.project_rbsg.RootController;
import de.uniks.se19.team_g.project_rbsg.ViewComponent;
import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.BattleFieldController;
import de.uniks.se19.team_g.project_rbsg.ingame.waiting_room.WaitingRoomViewController;
import de.uniks.se19.team_g.project_rbsg.ingame.waiting_room.event.GameEventManager;
import de.uniks.se19.team_g.project_rbsg.ingame.waiting_room.model.ModelManager;
import de.uniks.se19.team_g.project_rbsg.model.Game;
import de.uniks.se19.team_g.project_rbsg.termination.Terminable;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * TODO: consider removing scope prototype and instead introduce something like Bootable Interface for boot/terminate/boot/...
 */
@Component
@Scope("prototype")
public class IngameRootController
    implements RootController, Initializable, Terminable
{

    private static Logger logger = LoggerFactory.getLogger(IngameRootController.class);

    public StackPane root;

    @Nonnull
    private final ObjectFactory<ViewComponent<WaitingRoomViewController>> waitingRoomFactory;
    @Nonnull
    private final ObjectFactory<ViewComponent<BattleFieldController>> ingameFactory;
    @Nonnull
    private final ObjectFactory<IngameContext> contextFactory;
    @Nonnull
    private final GameEventManager gameEventManager;
    @Nonnull
    private final ModelManager modelManager;

    private IngameContext ingameContext;


    private ViewComponent<? extends IngameViewController> activeComponent;

    public IngameRootController(
        @Nonnull ObjectFactory<ViewComponent<WaitingRoomViewController>> waitingRoomFactory,
        @Nonnull ObjectFactory<ViewComponent<BattleFieldController>> ingameFactory,
        @Nonnull ObjectFactory<IngameContext> contextFactory,
        @Nonnull GameEventManager gameEventManager,
        @Nonnull ModelManager modelManager
    ) {
        this.waitingRoomFactory = waitingRoomFactory;
        this.ingameFactory = ingameFactory;
        this.contextFactory = contextFactory;
        this.gameEventManager = gameEventManager;
        this.modelManager = modelManager;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        configureContext();

        mountWaitingRoom();
    }

    protected void configureContext() {

        ingameContext = contextFactory.getObject();

        final Game gameData = Objects.requireNonNull(ingameContext.getGameData());

        gameEventManager.setOnConnectionClosed(this::onConnectionClosed);
        gameEventManager.addHandler(modelManager);
        gameEventManager.addHandler(this::handleGameInitFinished);

        try {
            gameEventManager.startSocket(gameData.getId(), null);
        } catch (Exception e) {
            logger.error("failed to start socket", e);
            // TODO: how to handle socket start error? so far, it escalated to FXML loader as well
            throw new RuntimeException(e);
        }

        ingameContext.setGameEventManager(gameEventManager);
    }

    protected void mountWaitingRoom() {
        activeComponent = waitingRoomFactory.getObject();
        activeComponent.getController().configure(ingameContext);

        final Node root = activeComponent.getRoot();
        this.root.getChildren().add(root);
    }

    @Override
    public void terminate() {
        if (activeComponent.getController() instanceof Terminable) {
            ((Terminable) activeComponent.getController()).terminate();
        }

        gameEventManager.terminate();
    }

    private void handleGameInitFinished(ObjectNode message) {
        if (GameEventManager.isActionType(message, GameEventManager.GAME_INIT_FINISHED)) {
            ingameContext.gameInitialized(modelManager.getGame());
        }
    }

    public void onConnectionClosed() {
        // alertBuilder.error(AlertBuilder.Text.CONNECTION_CLOSED, this::leaveWaitingRoom);
    }

}
