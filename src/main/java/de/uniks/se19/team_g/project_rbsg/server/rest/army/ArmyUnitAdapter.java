package de.uniks.se19.team_g.project_rbsg.server.rest.army;

import de.uniks.se19.team_g.project_rbsg.model.Unit;

public class ArmyUnitAdapter {
    public ArmyUnitAdapter() {
    }

    public Unit mapServerUnit(String id) {
        final Unit unit = new Unit();
        unit.id.set(id);
        return null;
    }
}