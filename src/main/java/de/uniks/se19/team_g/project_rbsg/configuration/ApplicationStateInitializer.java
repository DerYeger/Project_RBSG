package de.uniks.se19.team_g.project_rbsg.configuration;

import de.uniks.se19.team_g.project_rbsg.server.rest.army.persistance.PersistentArmyManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.units.GetUnitTypesService;
import javafx.application.Platform;
import org.springframework.lang.NonNull;
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

    private boolean createdDefaultArmies;
    @NonNull
    private PersistentArmyManager persistentArmyManager;

    public ApplicationStateInitializer(
            @Nonnull ApplicationState appState,
            @Nonnull ArmyManager armyManager,
            @Nonnull GetUnitTypesService getUnitTypesService,
            @Nonnull PersistentArmyManager persistentArmyManager
    ) {
        this.appState = appState;
        this.armyManager = armyManager;
        this.getUnitTypesService = getUnitTypesService;
        this.persistentArmyManager = persistentArmyManager;
    }

    public CompletableFuture<Void> initialize() {

        createdDefaultArmies = false;

        return getUnitTypesService.queryUnitPrototypes()
                .thenAcceptAsync(
                    appState.unitDefinitions::setAll,
                    Platform::runLater
                ).thenApply(
                    nothing -> {
                        return armyManager.getArmies();
                    }
                ).thenAcceptAsync(
                    armies -> {
                        appState.armies.setAll(armies);
                        try {
                            persistentArmyManager.saveArmies(appState.armies);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        if (createdDefaultArmies) {
                            Platform.runLater(() -> appState.notifications.add("army.newDefaultArmies"));
                        }
                    },
                    Platform::runLater
                )
        ;
    }
}
