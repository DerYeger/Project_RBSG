package de.uniks.se19.team_g.project_rbsg.ingame;

import de.uniks.se19.team_g.project_rbsg.ingame.event.GameEventManager;
import de.uniks.se19.team_g.project_rbsg.ingame.model.ModelManager;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import de.uniks.se19.team_g.project_rbsg.model.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Optional;

public class IngameContext {

    private User user;
    private Game gameData;
    private de.uniks.se19.team_g.project_rbsg.ingame.model.Game gameState;
    private ModelManager modelManager;
    private GameEventManager gameEventManager;

    private final BooleanProperty initialized = new SimpleBooleanProperty();
    private Player userPlayer;

    public IngameContext(
            User user,
            Game gameData
    ) {
        this.user = user;
        this.gameData = gameData;
    }


    public boolean isInitialized() {
        return initialized.get();
    }

    public ReadOnlyBooleanProperty initializedProperty() {
        return initialized;
    }

    public void gameInitialized(de.uniks.se19.team_g.project_rbsg.ingame.model.Game game) {

        User user = Objects.requireNonNull(getUser());

        gameState = game;
        initialized.set(true);

        Optional<Player> userPlayer = game.getPlayers().stream().filter(
                player -> player.getName().equals(user.getName())
        ).findAny();

        userPlayer.ifPresent(player -> player.setIsPlayer(true));

        this.userPlayer = userPlayer.orElse(null);
    }

    public User getUser() {
        return user;
    }

    public de.uniks.se19.team_g.project_rbsg.model.Game getGameData() {
        return gameData;
    }

    public de.uniks.se19.team_g.project_rbsg.ingame.model.Game getGameState() {
        return gameState;
    }

    public GameEventManager getGameEventManager() {
        return gameEventManager;
    }

    public void setGameEventManager(GameEventManager gameEventManager) {
        this.gameEventManager = gameEventManager;
    }

    public void tearDown() {
    }

    public boolean isMyTurn() {
        return getUserPlayer() == getGameState().getCurrentPlayer();
    }

    public Player getUserPlayer() {
        return userPlayer;
    }

    public ModelManager getModelManager() {
        return modelManager;
    }

    public void setModelManager(ModelManager modelManager) {

        this.modelManager = modelManager;
    }

    public void boot(boolean spectatorModus) {
        try {
            gameEventManager.startSocket(gameData.getId(), null, spectatorModus);
        } catch (Exception e) {
            // TODO: how to handle socket start error? so far, it escalated to FXML loader as well
            throw new RuntimeException(e);
        }
    }
}
