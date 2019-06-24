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
        final Unit u1 = new Unit();
        final Unit u2 = new Unit();
        final Unit u3 = new Unit();
        appState.unitDefinitions.addAll(u1, u2, u3);

        DefaultArmyGenerator sut = new DefaultArmyGenerator(appState);

        final Army army = sut.createArmy(null);

        Assert.assertEquals("My Army", army.name.get());
        Assert.assertNull(army.id.get());
        Assert.assertEquals(ApplicationState.ARMY_MAX_UNIT_COUNT, army.units.size());
        Assert.assertEquals(ApplicationState.ARMY_MAX_UNIT_COUNT / 3, army.units.filtered(unit -> unit == u1).size());
        Assert.assertEquals(ApplicationState.ARMY_MAX_UNIT_COUNT, army.units.size());
    }

}