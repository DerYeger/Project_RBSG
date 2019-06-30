package de.uniks.se19.team_g.project_rbsg.army_builder;

import de.uniks.se19.team_g.project_rbsg.configuration.SceneManagerConfig;
import de.uniks.se19.team_g.project_rbsg.ViewComponent;
import de.uniks.se19.team_g.project_rbsg.army_builder.army.ArmyDetailController;
import de.uniks.se19.team_g.project_rbsg.army_builder.unit_detail.UnitDetailController;
import de.uniks.se19.team_g.project_rbsg.army_builder.unit_detail.UnitPropertyController;
import de.uniks.se19.team_g.project_rbsg.army_builder.unit_selection.UnitListEntryController;
import de.uniks.se19.team_g.project_rbsg.army_builder.unit_selection.UnitListEntryFactory;
import de.uniks.se19.team_g.project_rbsg.configuration.ApplicationState;
import de.uniks.se19.team_g.project_rbsg.configuration.FXMLLoaderFactory;
import de.uniks.se19.team_g.project_rbsg.configuration.LocaleConfig;
import de.uniks.se19.team_g.project_rbsg.model.Unit;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.persistance.PersistentArmyManager;
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
import org.springframework.web.client.RestTemplate;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

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
        ApplicationState.class,
        SceneManagerConfig.class,
        PersistentArmyManager.class,
        RestTemplate.class,
        LocaleConfig.class,
        UnitPropertyController.class
})
public class ArmyBuilderViewTest extends ApplicationTest {


    @TestConfiguration
    static class ContextConfiguration {
        @Bean
        public ArmyDetailController armyDetailController() { return Mockito.mock(ArmyDetailController.class);}
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
                = (ViewComponent<ArmyBuilderController>) context.getBean("armyScene");
        final ArmyBuilderController controller = armyBuilderScene.getController();

        Assert.assertNotNull(controller.root);
        Assert.assertNotNull(controller.content);
        Assert.assertNotNull(controller.showInfoButton);
        Assert.assertNotNull(controller.soundButton);
        Assert.assertNotNull(controller.leaveButton);
        Assert.assertNotNull(controller.sideBarRight);
        Assert.assertNotNull(controller.sideBarLeft);
        Assert.assertNotNull(controller.unitDetailView);
        Assert.assertNotNull(controller.armyDetailsContainer);
        Assert.assertNotNull(controller.unitListView);
    }

    @Test
    public void testUnitList()
    {
        Unit unit = new Unit();
        unit.name.set("Archer");
        unit.iconUrl.set(getClass().getResource("/assets/icons/army/magicDefense.png").toString());

        @SuppressWarnings("unchecked") ViewComponent<ArmyBuilderController> armyBuilderComponent
                = (ViewComponent<ArmyBuilderController>) context.getBean("armyScene");

        Platform.runLater(() -> {
            stage.setScene(new Scene(armyBuilderComponent.getRoot()));
            stage.show();
        });
        WaitForAsyncUtils.waitForFxEvents();
        Assert.assertEquals(0, lookup(".list-cell .label").queryAll().size());

        Platform.runLater(() -> state.unitDefinitions.add(unit));
        WaitForAsyncUtils.waitForFxEvents();

        Assert.assertEquals(1, lookup(".list-cell .label").queryAll().size());

        Platform.runLater(() -> state.unitDefinitions.clear());
        WaitForAsyncUtils.waitForFxEvents();

        Assert.assertEquals(0, lookup(".list-cell .label").queryAll().size());

        Platform.runLater(() -> stage.hide());
        WaitForAsyncUtils.waitForFxEvents();
    }


}