package de.uniks.se19.team_g.project_rbsg.server.rest.army;

import de.uniks.se19.team_g.project_rbsg.model.Army;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.stream.Collectors;

@Component
public class ArmyAdapter {

    @Nonnull private final ArmyUnitAdapter armyUnitAdapter;

    public ArmyAdapter(@Nonnull ArmyUnitAdapter armyUnitAdapter) {
        this.armyUnitAdapter = armyUnitAdapter;
    }

    public Army mapArmyData(GetArmiesService.Response.Army serverArmy) {

        final Army localArmy = new Army();
        localArmy.id.set(serverArmy.id);
        localArmy.name.set(serverArmy.name);
        localArmy.units.setAll(serverArmy.units.stream().map(armyUnitAdapter::mapServerUnit).collect(Collectors.toList()));

        return localArmy;
    }

}