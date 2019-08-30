package de.uniks.se19.team_g.project_rbsg.configuration.flavour;

import de.uniks.se19.team_g.project_rbsg.configuration.flavor.*;
import de.uniks.se19.team_g.project_rbsg.ingame.model.*;
import org.junit.*;
import org.testfx.framework.junit.*;

import static org.junit.Assert.*;

public class TestUnitImageResolver extends ApplicationTest
{

    //Really crazy test
    //For the coverage!
    @Test
    public void testImageResolver() {
        UnitImageResolver.setFlavour(FlavourType.WH40K);

        assertNotNull(UnitImageResolver.getUnitImage(UnitTypeInfo._HEAVY_TANK));
        assertNotNull(UnitImageResolver.getUnitImage(UnitTypeInfo._BAZOOKA_TROOPER));
        assertNotNull(UnitImageResolver.getUnitImage(UnitTypeInfo._CHOPPER));
        assertNotNull(UnitImageResolver.getUnitImage(UnitTypeInfo._INFANTRY));
        assertNotNull(UnitImageResolver.getUnitImage(UnitTypeInfo._JEEP));
        assertNotNull(UnitImageResolver.getUnitImage(UnitTypeInfo._LIGHT_TANK));
        assertNotNull(UnitImageResolver.getUnitImage(UnitTypeInfo.UNKNOWN));

        UnitImageResolver.setFlavour(FlavourType.DEFAULT);

        assertNotNull(UnitImageResolver.getUnitImage(UnitTypeInfo._HEAVY_TANK, 50 ,50));
        assertNotNull(UnitImageResolver.getUnitImage(UnitTypeInfo._BAZOOKA_TROOPER, 50 ,50));
        assertNotNull(UnitImageResolver.getUnitImage(UnitTypeInfo._CHOPPER, 50 ,50));
        assertNotNull(UnitImageResolver.getUnitImage(UnitTypeInfo._INFANTRY, 50 ,50));
        assertNotNull(UnitImageResolver.getUnitImage(UnitTypeInfo._JEEP, 50 ,50));
        assertNotNull(UnitImageResolver.getUnitImage(UnitTypeInfo._LIGHT_TANK, 50 ,50));
        assertNotNull(UnitImageResolver.getUnitImage(UnitTypeInfo.UNKNOWN, 50 ,50));

        UnitImageResolver.setFlavour(FlavourType.WH40K);

        assertNotNull(UnitImageResolver.getUnitImageProperty(UnitTypeInfo._HEAVY_TANK));
        assertNotNull(UnitImageResolver.getUnitImageProperty(UnitTypeInfo._BAZOOKA_TROOPER));
        assertNotNull(UnitImageResolver.getUnitImageProperty(UnitTypeInfo._CHOPPER));
        assertNotNull(UnitImageResolver.getUnitImageProperty(UnitTypeInfo._INFANTRY));
        assertNotNull(UnitImageResolver.getUnitImageProperty(UnitTypeInfo._JEEP));
        assertNotNull(UnitImageResolver.getUnitImageProperty(UnitTypeInfo._LIGHT_TANK));
        assertNotNull(UnitImageResolver.getUnitImageProperty(UnitTypeInfo.UNKNOWN));

        assertNotNull(UnitImageResolver.getUnitImageURL(UnitTypeInfo._HEAVY_TANK));
        assertNotNull(UnitImageResolver.getUnitImageURL(UnitTypeInfo._BAZOOKA_TROOPER));
        assertNotNull(UnitImageResolver.getUnitImageURL(UnitTypeInfo._CHOPPER));
        assertNotNull(UnitImageResolver.getUnitImageURL(UnitTypeInfo._INFANTRY));
        assertNotNull(UnitImageResolver.getUnitImageURL(UnitTypeInfo._JEEP));
        assertNotNull(UnitImageResolver.getUnitImageURL(UnitTypeInfo._LIGHT_TANK));
        assertNotNull(UnitImageResolver.getUnitImageURL(UnitTypeInfo.UNKNOWN));
    }
}
