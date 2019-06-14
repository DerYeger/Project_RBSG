package de.uniks.se19.team_g.project_rbsg.configuration;

import de.uniks.se19.team_g.project_rbsg.model.Army;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class ArmyManager {
    @Nonnull
    public CompletionStage<List<Army>> getArmies() {
        return CompletableFuture.completedFuture(Collections.emptyList());
    }
}
