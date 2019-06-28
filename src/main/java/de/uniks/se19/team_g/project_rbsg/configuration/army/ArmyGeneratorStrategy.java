package de.uniks.se19.team_g.project_rbsg.configuration.army;

import de.uniks.se19.team_g.project_rbsg.model.Army;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public interface ArmyGeneratorStrategy {

    @Nonnull Army createArmy(@Nullable List<Army> armies);
}
