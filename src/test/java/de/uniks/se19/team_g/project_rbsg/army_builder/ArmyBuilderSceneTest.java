package de.uniks.se19.team_g.project_rbsg.army_builder;

import de.uniks.se19.team_g.project_rbsg.MusicManager;
import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.ViewComponent;
import de.uniks.se19.team_g.project_rbsg.army_builder.unit_detail.UnitDetailController;
import de.uniks.se19.team_g.project_rbsg.army_builder.unit_selection.UnitListEntryController;
import de.uniks.se19.team_g.project_rbsg.army_builder.unit_selection.UnitListEntryFactory;
import de.uniks.se19.team_g.project_rbsg.configuration.FXMLLoaderFactory;
import de.uniks.se19.team_g.project_rbsg.model.Unit;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.GetUnitTypesService;
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
        SceneController.class,
        UnitDetailController.class,
        UnitListEntryFactory.class,
        UnitListEntryController.class,
        ArmyBuilderSceneTest.ContextConfiguration.class,
        MusicManager.class,
        SceneManager.class
})
public class ArmyBuilderSceneTest extends ApplicationTest {


    @TestConfiguration
    static class ContextConfiguration {
        @Bean
        public GetUnitTypesService getUnitTypesService()
        {
            final Unit unit = new Unit();
            unit.iconUrl.set(getClass().getResource("/assets/icons/army/magic-defense.png").toString());
            unit.name.set("novice");
            final GetUnitTypesService mock = Mockito.mock(GetUnitTypesService.class);
            Mockito.when(mock.queryUnitPrototypes()).thenReturn(CompletableFuture.completedFuture(Collections.singletonList(unit)));
            return mock;
        }
    }

    @Autowired
    public ApplicationContext context;

    @Autowired
    public ArmyBuilderState state;
    private Stage stage;

    @Override
    public void start(Stage stage) {

        this.stage = stage;
    }

    @Test
    public void testSceneCreation()
    {
        @SuppressWarnings("unchecked") ViewComponent<SceneController> armyBuilderScene
                = (ViewComponent<SceneController>) context.getBean("armyBuilderScene");
        final SceneController controller = armyBuilderScene.getController();

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
        unit.iconUrl.set(getClass().getResource("/assets/icons/army/magic-defense.png").toString());

        Assert.assertEquals(0, state.unitTypes.size());

        @SuppressWarnings("unchecked") ViewComponent<SceneController> armyBuilderComponent
                = (ViewComponent<SceneController>) context.getBean("armyBuilderScene");
        Assert.assertEquals(1, state.unitTypes.size());

        Platform.runLater(() -> {
            stage.setScene(new Scene(armyBuilderComponent.getRoot()));
            stage.show();
        });
        WaitForAsyncUtils.waitForFxEvents();
        Assert.assertEquals(1, lookup(".list-cell #imageView").queryAll().size());

        Platform.runLater(() -> state.unitTypes.add(unit));
        WaitForAsyncUtils.waitForFxEvents();

        Assert.assertEquals(2, lookup(".list-cell #imageView").queryAll().size());

        Platform.runLater(() -> state.unitTypes.clear());
        WaitForAsyncUtils.waitForFxEvents();

        Assert.assertEquals(0, lookup(".list-cell #imageView").queryAll().size());

        Platform.runLater(() -> stage.hide());
        WaitForAsyncUtils.waitForFxEvents();
    }


}