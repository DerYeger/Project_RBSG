package de.uniks.se19.team_g.project_rbsg.server.rest.army;

import de.uniks.se19.team_g.project_rbsg.model.Unit;

public class UnitTypeAdapter {

    public Unit map(UnitType unitType) {
        final Unit unit = new Unit();
        unit.iconUrl.set(getClass().getResource("/assets/icons/army/magic-defense.png").toString());
        unit.imageUrl.set(getClass().getResource("/assets/sprites/Soldier.png").toString());
        unit.name.set(unitType.type);
        unit.description.set(unitType.id);
        unit.speed.set(unitType.mp);
        unit.health.set(unitType.hp);
        return unit;
    }
}
