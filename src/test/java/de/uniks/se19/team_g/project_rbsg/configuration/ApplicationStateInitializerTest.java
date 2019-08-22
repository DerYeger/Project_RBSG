package de.uniks.se19.team_g.project_rbsg.configuration;

import de.uniks.se19.team_g.project_rbsg.configuration.army.ArmyGeneratorStrategy;
import de.uniks.se19.team_g.project_rbsg.model.Army;
import de.uniks.se19.team_g.project_rbsg.model.Unit;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.persistance.PersistentArmyManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.units.GetUnitTypesService;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InOrder;
import org.testfx.framework.junit.ApplicationTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.*;

public class ApplicationStateInitializerTest extends ApplicationTest {

    @Test
    public void initialize() throws ExecutionException, InterruptedException {

        final ApplicationState appState = new ApplicationState();

        final GetUnitTypesService typesService = mock(GetUnitTypesService.class);
        final List<Unit> types = Collections.emptyList();
        when(typesService.queryUnitPrototypes()).thenReturn(CompletableFuture.completedFuture(types));

        final ArmyManager armyManager = mock(ArmyManager.class);
        final List<Army> armies = new ArrayList<>();
        when(armyManager.getArmies()).thenReturn(armies);

        final ArmyGeneratorStrategy armyGenerator = mock(ArmyGeneratorStrategy.class);
        final PersistentArmyManager persistentArmyManager = mock(PersistentArmyManager.class);

        ApplicationStateInitializer sut = new ApplicationStateInitializer(
                appState,
                armyManager,
                typesService,
                armyGenerator,
                persistentArmyManager
        );

        final InOrder inOrder = inOrder(typesService, armyManager, armyGenerator);

        sut.initialize().get();

        inOrder.verify(typesService).queryUnitPrototypes();
        inOrder.verify(armyManager).getArmies();
        inOrder.verify(armyGenerator, times(ApplicationState.MAX_ARMY_COUNT)).createArmy(armies);

        Assert.assertEquals(ApplicationState.MAX_ARMY_COUNT, appState.armies.size());
        Assert.assertEquals(1, appState.notifications.size());
    }

    /*
    @Test
    public void fillArmies() {
        final ApplicationState appState = mock(ApplicationState.class);
        final GetUnitTypesService typesService = mock(GetUnitTypesService.class);
        final ArmyManager armyManager = mock(ArmyManager.class);
        final ArmyGeneratorStrategy armyGenerator = mock(ArmyGeneratorStrategy.class);
        final PersistentArmyManager persistentArmyManager = mock(PersistentArmyManager.class);

        ApplicationStateInitializer sut = new ApplicationStateInitializer(
                appState,
                armyManager,
                typesService,
                armyGenerator,
                persistentArmyManager
        );

        List<Army> armies = new ArrayList<>();
        when(armyGenerator.createArmy(armies)).thenReturn(new Army());

        sut.fillArmies(armies);
        verify(armyGenerator, times(ApplicationState.MAX_ARMY_COUNT)).createArmy(armies);

        Assert.assertEquals(ApplicationState.MAX_ARMY_COUNT, armies.size());
    }

     */
}