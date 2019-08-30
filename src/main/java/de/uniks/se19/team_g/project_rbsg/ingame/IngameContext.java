package de.uniks.se19.team_g.project_rbsg.ingame;

import de.uniks.se19.team_g.project_rbsg.ingame.event.GameEventManager;
import de.uniks.se19.team_g.project_rbsg.ingame.model.ModelManager;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import de.uniks.se19.team_g.project_rbsg.model.Game;
import de.uniks.se19.team_g.project_rbsg.model.User;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

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

        Optional<Player> userPlayer = game.getPlayers().stream().filter(
                player -> player.getName().equals(user.getName())
        ).findAny();

        userPlayer.ifPresent(player -> player.setIsPlayer(true));

        this.userPlayer = userPlayer.orElse(null);
        if (game.getCurrentPlayer() != null) {
            onNextPlayer(null, null, game.getCurrentPlayer());
        }
        game.currentPlayerProperty().addListener(this::onNextPlayer);

        initialized.set(true);
    }

    private void onNextPlayer (Observable observable, Player lastPlayer, Player nextPlayer)
    {
        if (isMyTurn()) {
            onBeforeUserTurn();
        }
    }

    private void onBeforeUserTurn ()
    {
        for (de.uniks.se19.team_g.project_rbsg.ingame.model.Unit unit : getUserPlayer().getUnits())
        {
            unit.setRemainingMovePoints(unit.getMp());
        }
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

    public IngameContext boot(boolean spectatorModus) {
        try {
            gameEventManager.startSocket(gameData.getId(), null, spectatorModus);
        } catch (Exception e) {
            // TODO: how to handle socket start error? so far, it escalated to FXML loader as well
            throw new RuntimeException(e);
        }

        return this;
    }
}
