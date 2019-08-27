package de.uniks.se19.team_g.project_rbsg.configuration.army;

import de.uniks.se19.team_g.project_rbsg.configuration.ApplicationState;
import de.uniks.se19.team_g.project_rbsg.configuration.flavor.UnitTypeInfo;
import de.uniks.se19.team_g.project_rbsg.model.Army;
import de.uniks.se19.team_g.project_rbsg.model.Unit;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Component
public class ChubbyCharlesCharge implements ArmyGeneratorStrategy {

    @Nonnull
    private final ApplicationState appState;
    @Nonnull
    private final DefaultArmyGenerator fallback;

    public ChubbyCharlesCharge(
        @Nonnull ApplicationState appState,
        @Nonnull DefaultArmyGenerator fallback
    ) {
        this.appState = appState;
        this.fallback = fallback;
    }

    @Nonnull
    @Override
    public Army createArmy(@Nullable List<Army> armies) {

        Unit chubby = null;
        for (Unit definition : appState.unitDefinitions) {
            if (definition.getTypeInfo() == UnitTypeInfo._HEAVY_TANK) {
                chubby = definition;
                break;
            }
        }

        if (chubby == null) {
            return fallback.createArmy(armies);
        }

        Army army = new Army();
        army.name.set("chubby-charles-charge");

        for (int i = 0; i < ApplicationState.ARMY_MAX_UNIT_COUNT; i++) {
            army.units.add(chubby.clone());
        }

        return army;
    }

}
