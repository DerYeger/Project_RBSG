package de.uniks.se19.team_g.project_rbsg.configuration.army;

import de.uniks.se19.team_g.project_rbsg.ProjectRbsgFXApplication;
import de.uniks.se19.team_g.project_rbsg.configuration.ApplicationState;
import de.uniks.se19.team_g.project_rbsg.model.Army;
import de.uniks.se19.team_g.project_rbsg.model.Unit;
import io.rincl.Rincl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DefaultArmyGenerator implements ArmyGeneratorStrategy {

    @Nonnull
    private final ApplicationState appState;

    public DefaultArmyGenerator(@Nonnull ApplicationState appState) {
        this.appState = appState;
    }

    @Nonnull
    @Override
    public Army createArmy(@Nullable List<Army> armies) {
        final Army army = getArmy();

        final List<Unit> units = new ArrayList<>();

        fillLoop:
        while (true) {
            for (Unit unitDefinition : appState.unitDefinitions) {
                if (units.size() < ApplicationState.ARMY_MAX_UNIT_COUNT) {
                    units.add(unitDefinition.clone());
                } else {
                    break fillLoop;
                }
            }
        }

        army.units.addAll(units);

        return army;
    }

    private Army getArmy() {
        Army army = new Army();
        army.name.set(
            Rincl.getResources(ProjectRbsgFXApplication.class).getString("army.defaultName")
        );
        return army;
    }
}
