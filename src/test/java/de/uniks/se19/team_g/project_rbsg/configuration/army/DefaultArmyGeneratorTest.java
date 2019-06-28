package de.uniks.se19.team_g.project_rbsg.configuration.army;

import de.uniks.se19.team_g.project_rbsg.configuration.ApplicationState;
import de.uniks.se19.team_g.project_rbsg.model.Army;
import de.uniks.se19.team_g.project_rbsg.model.Unit;
import io.rincl.Rincl;
import io.rincl.resourcebundle.ResourceBundleResourceI18nConcern;
import org.junit.Assert;
import org.junit.Test;

import java.util.Locale;

public class DefaultArmyGeneratorTest {

    @Test
    public void test()
    {
        Rincl.setDefaultResourceI18nConcern(new ResourceBundleResourceI18nConcern());
        Rincl.setLocale(Locale.ENGLISH);
        final ApplicationState appState = new ApplicationState();
        final Unit u1 = Unit.unknownType("1");
        final Unit u2 = Unit.unknownType("2");
        final Unit u3 = Unit.unknownType("3");
        appState.unitDefinitions.addAll(u1, u2, u3);
        final int definitionCount = appState.unitDefinitions.size();

        DefaultArmyGenerator sut = new DefaultArmyGenerator(appState);

        final Army army = sut.createArmy(null);

        Assert.assertEquals("My Army", army.name.get());
        Assert.assertNull(army.id.get());
        Assert.assertEquals(ApplicationState.ARMY_MAX_UNIT_COUNT, army.units.size());
        Assert.assertEquals(ApplicationState.ARMY_MAX_UNIT_COUNT / definitionCount + ApplicationState.ARMY_MAX_UNIT_COUNT % definitionCount, army.units.filtered(u1::equals).size());
        Assert.assertEquals(ApplicationState.ARMY_MAX_UNIT_COUNT / definitionCount, army.units.filtered(u3::equals).size());
        Assert.assertEquals(ApplicationState.ARMY_MAX_UNIT_COUNT, army.units.size());
    }

}