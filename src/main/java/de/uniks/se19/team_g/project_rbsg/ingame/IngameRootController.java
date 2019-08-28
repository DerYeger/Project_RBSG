package de.uniks.se19.team_g.project_rbsg.ingame;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.uniks.se19.team_g.project_rbsg.RootController;
import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.ViewComponent;
import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.BattleFieldController;
import de.uniks.se19.team_g.project_rbsg.ingame.event.GameEventManager;
import de.uniks.se19.team_g.project_rbsg.ingame.model.ModelManager;
import de.uniks.se19.team_g.project_rbsg.ingame.state.GameEventDispatcher;
import de.uniks.se19.team_g.project_rbsg.ingame.waiting_room.WaitingRoomViewController;
import de.uniks.se19.team_g.project_rbsg.model.Game;
import de.uniks.se19.team_g.project_rbsg.overlay.alert.AlertBuilder;
import de.uniks.se19.team_g.project_rbsg.termination.Terminable;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
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
    private final ObjectFactory<ViewComponent<BattleFieldController>> battleFieldFactory;
    @Nonnull
    private final ObjectFactory<IngameContext> contextFactory;
    @Nonnull
    private final SceneManager sceneManager;
    @Nonnull
    private final AlertBuilder alertBuilder;

    private IngameContext ingameContext;

    private ViewComponent<? extends IngameViewController> activeComponent;

    public IngameRootController(
            @Nonnull ObjectFactory<ViewComponent<WaitingRoomViewController>> waitingRoomFactory,
            @Nonnull ObjectFactory<ViewComponent<BattleFieldController>> battleFieldFactory,
            @Nonnull @Qualifier("autoContext") ObjectFactory<IngameContext> contextFactory,
            @Nonnull SceneManager sceneManager,
            @Nonnull AlertBuilder alertBuilder
    ) {
        this.waitingRoomFactory = waitingRoomFactory;
        this.battleFieldFactory = battleFieldFactory;
        this.contextFactory = contextFactory;
        this.sceneManager = sceneManager;
        this.alertBuilder = alertBuilder;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        configureContext();

        mountWaitingRoom();
    }

    public void configureContext() {

        ingameContext = contextFactory.getObject();

        Game gameData = ingameContext.getGameData();
        GameEventManager gameEventManager = ingameContext.getGameEventManager();

        gameEventManager.setOnConnectionClosed(this::onConnectionClosed);
        gameEventManager.addHandler(this::handleGameEvents);

        boolean spectatorModus = ingameContext.getGameData().isSpectatorModus();

        ingameContext.boot(spectatorModus);
    }

    @Override
    public void terminate() {
        ingameContext.getGameEventManager().terminate();

        if (activeComponent != null && activeComponent.getController() instanceof Terminable) {
            ((Terminable) activeComponent.getController()).terminate();
        }

        ingameContext.tearDown();
    }

    // package-private for testability. i'm so sorry.
    void handleGameEvents(ObjectNode message) {
        if (GameEventManager.isActionType(message, GameEventManager.GAME_STARTS)) {
            Platform.runLater(this::mountBattleField);
        }
    }

    public void onConnectionClosed() {
        alertBuilder.information(AlertBuilder.Text.CONNECTION_CLOSED, this::leave);
    }

    protected void mountBattleField() {
        mountContent(battleFieldFactory.getObject());
        sceneManager.setResizeableTrue();
    }

    protected void mountWaitingRoom() {
        sceneManager.setResizeableFalse();
        mountContent(waitingRoomFactory.getObject());
    }

    protected void mountContent(ViewComponent<? extends IngameViewController> nextComponent) {

        if (activeComponent != null) {
            // might terminate old component?
            if (activeComponent.getController() instanceof Terminable) {
                ((Terminable) activeComponent.getController()).terminate();
            }
            root.getChildren().remove(activeComponent.getRoot());
        }
        root.getChildren().add(nextComponent.getRoot());
        nextComponent.getRoot().toBack();
        nextComponent.getController().configure(ingameContext);
        activeComponent = nextComponent;
    }

    private void leave() {
        sceneManager.setScene(SceneManager.SceneIdentifier.LOBBY, false, null);
    }

}
