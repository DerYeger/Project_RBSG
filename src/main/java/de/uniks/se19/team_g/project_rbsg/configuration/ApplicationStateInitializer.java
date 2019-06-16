package de.uniks.se19.team_g.project_rbsg.configuration;

import de.uniks.se19.team_g.project_rbsg.server.rest.army.units.GetUnitTypesService;
import javafx.application.Platform;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
public class ApplicationStateInitializer {

    @Nonnull
    private final ApplicationState appState;
    @Nonnull
    private final ArmyManager armyManager;
    @Nonnull
    private final GetUnitTypesService getUnitTypesService;

    public ApplicationStateInitializer(
            @Nonnull ApplicationState appState,
            @Nonnull ArmyManager armyManager,
            @Nonnull GetUnitTypesService getUnitTypesService
    ) {
        this.appState = appState;
        this.armyManager = armyManager;
        this.getUnitTypesService = getUnitTypesService;
    }

    public CompletableFuture<Void> initialize() {

        return getUnitTypesService.queryUnitPrototypes()
                .thenAcceptAsync(
                    appState.unitDefinitions::setAll,
                    Platform::runLater
                ).thenApply(
                    nothing -> {
                        try {
                            return armyManager.getArmies().get();
                        } catch (InterruptedException | ExecutionException e) {
                            throw new RuntimeException(e);
                        }
                    }
                ).thenAcceptAsync(
                    appState.armies::setAll,
                    Platform::runLater
                )
        ;
    }
}
