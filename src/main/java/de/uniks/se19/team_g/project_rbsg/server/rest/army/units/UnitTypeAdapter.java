package de.uniks.se19.team_g.project_rbsg.server.rest.army.units;

import de.uniks.se19.team_g.project_rbsg.model.Unit;
import de.uniks.se19.team_g.project_rbsg.model.UnitTypeMetaData;
import org.springframework.stereotype.Component;

@Component
public class UnitTypeAdapter {

    public Unit map(UnitType unitType) {
        final Unit unit = new Unit();

        UnitTypeMetaData metaData = getMetaDataForType(unitType.type);

        unit.id.set(unitType.id);
        unit.type.set(unitType.type);

        unit.name.set(unitType.type);
        unit.iconUrl.set(metaData.getIcon().toString());
        unit.imageUrl.set(metaData.getImage().toString());

        unit.description.set(unitType.id);
        unit.speed.set(unitType.mp);
        unit.health.set(unitType.hp);

        unit.canAttack.setAll(unitType.canAttack);

        unit.description.set(mapDescription(unitType));

        return unit;
    }

    private UnitTypeMetaData getMetaDataForType(final String type) {
        String asCamelCaseType = type.replace(" ", "");

        try {
            return UnitTypeMetaData.valueOf(asCamelCaseType);
        } catch (IllegalArgumentException e) {
            return UnitTypeMetaData.UNKNOWN;
        }
    }

    private String mapDescription(UnitType unitType) {
        return String.format(
            "id: %s\n",
            unitType.id
        );
    }
}
