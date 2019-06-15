package de.uniks.se19.team_g.project_rbsg.server.rest.army.units;

import de.uniks.se19.team_g.project_rbsg.model.Unit;
import org.springframework.stereotype.Component;

@Component
public class UnitTypeAdapter {

    public Unit map(UnitType unitType) {
        final Unit unit = new Unit();
        unit.iconUrl.set(getClass().getResource("/assets/icons/army/magicDefense.png").toString());
        unit.imageUrl.set(getClass().getResource("/assets/sprites/soldier.png").toString());
        unit.name.set(unitType.type);
        unit.description.set(unitType.id);
        unit.speed.set(unitType.mp);
        unit.health.set(unitType.hp);

        unit.description.set(mapDescription(unitType));

        return unit;
    }

    private String mapDescription(UnitType unitType) {
        return String.format(
            "id: %s\n"+
            "Can attack %s",
            unitType.id,
            String.join(", ", unitType.canAttack)
        );
    }
}
