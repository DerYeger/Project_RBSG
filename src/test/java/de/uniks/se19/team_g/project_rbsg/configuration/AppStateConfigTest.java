package de.uniks.se19.team_g.project_rbsg.configuration;

import de.uniks.se19.team_g.project_rbsg.model.Army;
import de.uniks.se19.team_g.project_rbsg.model.Unit;
import org.junit.Test;

import static org.junit.Assert.*;

public class AppStateConfigTest {

    @Test
    public void testAppStateConfiguration() {
        ApplicationState state = new AppStateConfig().appState();

        assertFalse(state.hasPlayableArmies.get());
        assertNull(state.selectedArmy.get());

        Army army = new Army();
        state.armies.add(army);

        assertSame(army, state.selectedArmy.get());

        Army army2 = new Army();
        state.armies.add(army2);

        assertSame(army, state.selectedArmy.get());

        assertFalse(state.hasPlayableArmies.get());
        army.id.set("1");
        assertFalse(state.hasPlayableArmies.get());
        for (int i = 0; i < ApplicationState.ARMY_MAX_UNIT_COUNT; i++) {
            army.units.add(new Unit());
        }
        assertTrue(state.hasPlayableArmies.get());
        army.id.set(null);
        assertFalse(state.hasPlayableArmies.get());
    }
}