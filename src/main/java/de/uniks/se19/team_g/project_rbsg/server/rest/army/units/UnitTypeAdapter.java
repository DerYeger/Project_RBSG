package de.uniks.se19.team_g.project_rbsg.server.rest.army.units;

import de.uniks.se19.team_g.project_rbsg.model.Unit;
import de.uniks.se19.team_g.project_rbsg.configuration.flavor.UnitTypeInfo;
import org.springframework.stereotype.Component;

import java.net.URL;

@Component
public class UnitTypeAdapter {

    public Unit map(de.uniks.se19.team_g.project_rbsg.server.rest.army.units.UnitType unitType) {
        final Unit unit = new Unit();

        UnitTypeInfo typeInfo = getTypeInfo(unitType);

        unit.id.set(unitType.id);
        unit.type.set(unitType.type);

        unit.setTypeInfo(typeInfo);

        unit.name.set(unitType.type);
        unit.iconUrl.set(typeInfo.getIcon().toExternalForm());
        final URL image = typeInfo.getImage();
        final String newValue = image.toExternalForm();
        unit.imageUrl.set(newValue);

        unit.description.set(typeInfo.getDescriptionKey());
        unit.speed.set(unitType.mp);
        unit.health.set(unitType.hp);

        unit.canAttack.setAll(unitType.canAttack);

        unit.description.set(mapDescription(unitType));

        return unit;
    }

    private UnitTypeInfo getTypeInfo(final de.uniks.se19.team_g.project_rbsg.server.rest.army.units.UnitType unitType) {
        String identifier = "_" + unitType.id;

        try {
            return UnitTypeInfo.valueOf(identifier);
        } catch (IllegalArgumentException e) {
            return UnitTypeInfo.UNKNOWN;
        }
    }

    private String mapDescription(de.uniks.se19.team_g.project_rbsg.server.rest.army.units.UnitType unitType) {
        return String.format(
            "id: %s\n",
            unitType.id
        );
    }
}
