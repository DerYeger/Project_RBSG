package de.uniks.se19.team_g.project_rbsg.configuration;

import javafx.application.Platform;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

@Component
public class ApplicationStateInitializer {

    @Nonnull private final ApplicationState appState;
    @Nonnull private final ArmyManager armyManager;

    public ApplicationStateInitializer(
            @Nonnull ApplicationState appState,
            @Nonnull ArmyManager armyManager
    ) {
        this.appState = appState;
        this.armyManager = armyManager;
    }

    public CompletableFuture<Void> initialize() {
        return CompletableFuture.allOf(
            armyManager.getArmies().thenAccept(armies -> Platform.runLater(() -> appState.armies.setAll(armies)))
        );
    }
}
