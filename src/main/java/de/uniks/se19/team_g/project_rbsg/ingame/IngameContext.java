package de.uniks.se19.team_g.project_rbsg.ingame;

import de.uniks.se19.team_g.project_rbsg.ingame.event.GameEventManager;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import de.uniks.se19.team_g.project_rbsg.model.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.Objects;

@Component
@Scope("prototype")
public class IngameContext {

    @Nonnull private final UserProvider userProvider;
    @Nonnull private final GameProvider gameDataProvider;
    @Nonnull private final IngameGameProvider gameStateProvider;
    private GameEventManager gameEventManager;

    private final BooleanProperty initialized = new SimpleBooleanProperty();
    private Player userPlayer;

    public IngameContext(
        @Nonnull UserProvider userProvider,
        @Nonnull GameProvider gameDataProvider,
        @Nonnull IngameGameProvider gameStateProvider
    ) {
        this.userProvider = userProvider;
        this.gameDataProvider = gameDataProvider;
        this.gameStateProvider = gameStateProvider;
    }


    public boolean isInitialized() {
        return initialized.get();
    }

    public ReadOnlyBooleanProperty initializedProperty() {
        return initialized;
    }

    public void gameInitialized(de.uniks.se19.team_g.project_rbsg.ingame.model.Game game) {

        User user = Objects.requireNonNull(getUser());

        gameStateProvider.set(game);
        initialized.set(true);

        userPlayer = game.getPlayers().stream().filter(
                player -> player.getName().equals(user.getName())
        ).findAny().orElse(null);
    }

    public User getUser() {
        return userProvider.get();
    }

    public de.uniks.se19.team_g.project_rbsg.model.Game getGameData() {
        return gameDataProvider.get();
    }

    public de.uniks.se19.team_g.project_rbsg.ingame.model.Game getGameState() {
        return gameStateProvider.get();
    }

    public GameEventManager getGameEventManager() {
        return gameEventManager;
    }

    public void setGameEventManager(GameEventManager gameEventManager) {
        this.gameEventManager = gameEventManager;
    }

    public void tearDown() {
        gameDataProvider.clear();
        gameStateProvider.clear();
    }

    public boolean isMyTurn() {
        return getUserPlayer() == getGameState().getCurrentPlayer();
    }

    public Player getUserPlayer() {
        return userPlayer;
    }
}
