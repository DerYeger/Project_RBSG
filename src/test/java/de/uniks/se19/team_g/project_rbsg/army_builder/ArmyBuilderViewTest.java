package de.uniks.se19.team_g.project_rbsg.army_builder;

import de.uniks.se19.team_g.project_rbsg.ViewComponent;
import de.uniks.se19.team_g.project_rbsg.army_builder.unit_detail.UnitDetailController;
import de.uniks.se19.team_g.project_rbsg.army_builder.unit_selection.UnitListEntryController;
import de.uniks.se19.team_g.project_rbsg.army_builder.unit_selection.UnitListEntryFactory;
import de.uniks.se19.team_g.project_rbsg.configuration.ApplicationState;
import de.uniks.se19.team_g.project_rbsg.configuration.FXMLLoaderFactory;
import de.uniks.se19.team_g.project_rbsg.model.Unit;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.units.GetUnitTypesService;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

/**
 * @author Goatfryed
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        ArmyBuilderConfig.class,
        FXMLLoaderFactory.class,
        ArmyBuilderController.class,
        UnitDetailController.class,
        UnitListEntryFactory.class,
        UnitListEntryController.class,
        ArmyBuilderViewTest.ContextConfiguration.class,
        ApplicationState.class
})
public class ArmyBuilderViewTest extends ApplicationTest {


    @TestConfiguration
    static class ContextConfiguration {
        @Bean
        public GetUnitTypesService getUnitTypesService()
        {
            final Unit unit = new Unit();
            unit.iconUrl.set(getClass().getResource("/assets/icons/army/magicDefense.png").toString());
            unit.name.set("novice");
            final GetUnitTypesService mock = Mockito.mock(GetUnitTypesService.class);
            Mockito.when(mock.queryUnitPrototypes()).thenReturn(CompletableFuture.completedFuture(Collections.singletonList(unit)));
            return mock;
        }
    }

    @Autowired
    public ApplicationContext context;

    @Autowired
    public ApplicationState state;
    private Stage stage;

    @Override
    public void start(Stage stage) {

        this.stage = stage;
    }

    @Test
    public void testSceneCreation()
    {
        @SuppressWarnings("unchecked") ViewComponent<ArmyBuilderController> armyBuilderScene
                = (ViewComponent<ArmyBuilderController>) context.getBean("armyBuilderScene");
        final ArmyBuilderController controller = armyBuilderScene.getController();

        Assert.assertNotNull(controller.root);
        Assert.assertNotNull(controller.content);
        Assert.assertNotNull(controller.soundButton);
        Assert.assertNotNull(controller.leaveButton);
        Assert.assertNotNull(controller.sideBarRight);
        Assert.assertNotNull(controller.sideBarLeft);
        Assert.assertNotNull(controller.unitDetailView);
        Assert.assertNotNull(controller.armyView);
        Assert.assertNotNull(controller.unitListView);
    }

    @Test
    public void testUnitList()
    {
        Unit unit = new Unit();
        unit.name.set("Archer");
        unit.iconUrl.set(getClass().getResource("/assets/icons/army/magicDefense.png").toString());

        Assert.assertEquals(0, state.unitTypeDefinitions.size());

        @SuppressWarnings("unchecked") ViewComponent<ArmyBuilderController> armyBuilderComponent
                = (ViewComponent<ArmyBuilderController>) context.getBean("armyBuilderScene");
        Assert.assertEquals(1, state.unitTypeDefinitions.size());

        Platform.runLater(() -> {
            stage.setScene(new Scene(armyBuilderComponent.getRoot()));
            stage.show();
        });
        WaitForAsyncUtils.waitForFxEvents();
        Assert.assertEquals(1, lookup(".list-cell #imageView").queryAll().size());

        Platform.runLater(() -> state.unitTypeDefinitions.add(unit));
        WaitForAsyncUtils.waitForFxEvents();

        Assert.assertEquals(2, lookup(".list-cell #imageView").queryAll().size());

        Platform.runLater(() -> state.unitTypeDefinitions.clear());
        WaitForAsyncUtils.waitForFxEvents();

        Assert.assertEquals(0, lookup(".list-cell #imageView").queryAll().size());

        Platform.runLater(() -> stage.hide());
        WaitForAsyncUtils.waitForFxEvents();
    }


}