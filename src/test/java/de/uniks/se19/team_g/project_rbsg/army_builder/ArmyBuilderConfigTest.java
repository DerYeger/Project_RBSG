package de.uniks.se19.team_g.project_rbsg.army_builder;

import de.uniks.se19.team_g.project_rbsg.configuration.ApplicationState;
import de.uniks.se19.team_g.project_rbsg.model.Army;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class ArmyBuilderConfigTest {

    @Test
    public void armyBuilderState() {

        ArmyBuilderConfig sut = new ArmyBuilderConfig();
        ApplicationState appState = new ApplicationState();

        Army army = new Army();
        army.setUnsavedUpdates(true);

        final ArmyBuilderState armyBuilderState = sut.armyBuilderState(appState);

        Assert.assertFalse(armyBuilderState.hasUnsavedUpdates());

        appState.armies.add(army);
        Assert.assertTrue(armyBuilderState.hasUnsavedUpdates());

        appState.armies.remove(army);
        Assert.assertFalse(armyBuilderState.hasUnsavedUpdates());

        appState.armies.add(new Army());
        appState.armies.add(army);
        army.setUnsavedUpdates(false);
        Assert.assertFalse(armyBuilderState.hasUnsavedUpdates());

        army.setUnsavedUpdates(true);
        Assert.assertTrue(armyBuilderState.hasUnsavedUpdates());
    }
}