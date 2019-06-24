package de.uniks.se19.team_g.project_rbsg.configuration;

import de.uniks.se19.team_g.project_rbsg.configuration.army.ArmyGeneratorStrategy;
import de.uniks.se19.team_g.project_rbsg.model.Army;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.units.GetUnitTypesService;
import javafx.application.Platform;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
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
    @Nullable
    private ArmyGeneratorStrategy armyGeneratorStrategy;

    public ApplicationStateInitializer(
            @Nonnull ApplicationState appState,
            @Nonnull ArmyManager armyManager,
            @Nonnull GetUnitTypesService getUnitTypesService,
            @Nullable ArmyGeneratorStrategy armyGeneratorStrategy
    ) {
        this.appState = appState;
        this.armyManager = armyManager;
        this.getUnitTypesService = getUnitTypesService;
        this.armyGeneratorStrategy = armyGeneratorStrategy;
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
                ).thenApply(this::fillArmies)
                .thenAcceptAsync(
                    appState.armies::setAll,
                    Platform::runLater
                )
        ;
    }

    public List<Army> fillArmies(List<Army> armies) {
        if (armyGeneratorStrategy != null) {
            while (armies.size() < ApplicationState.MAX_ARMY_COUNT) {
                armies.add(armyGeneratorStrategy.createArmy(armies));
            }
        }

        return armies;
    }
}
