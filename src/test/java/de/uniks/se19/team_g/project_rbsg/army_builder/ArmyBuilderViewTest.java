package de.uniks.se19.team_g.project_rbsg.army_builder;

import de.uniks.se19.team_g.project_rbsg.scene.SceneManager;
import de.uniks.se19.team_g.project_rbsg.scene.ViewComponent;
import de.uniks.se19.team_g.project_rbsg.army_builder.army.ArmyDetailController;
import de.uniks.se19.team_g.project_rbsg.army_builder.edit_army.EditArmyController;
import de.uniks.se19.team_g.project_rbsg.army_builder.unit_detail.UnitDetailController;
import de.uniks.se19.team_g.project_rbsg.army_builder.unit_detail.UnitPropertyController;
import de.uniks.se19.team_g.project_rbsg.army_builder.unit_selection.UnitListCellController;
import de.uniks.se19.team_g.project_rbsg.army_builder.unit_selection.UnitListCellFactory;
import de.uniks.se19.team_g.project_rbsg.configuration.ApplicationState;
import de.uniks.se19.team_g.project_rbsg.configuration.FXMLLoaderFactory;
import de.uniks.se19.team_g.project_rbsg.configuration.LocaleConfig;
import de.uniks.se19.team_g.project_rbsg.configuration.SceneManagerConfig;
import de.uniks.se19.team_g.project_rbsg.configuration.army.DefaultArmyGenerator;
import de.uniks.se19.team_g.project_rbsg.model.Army;
import de.uniks.se19.team_g.project_rbsg.model.Unit;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.overlay.alert.AlertBuilder;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.ArmyAdapter;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.ArmyUnitAdapter;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.GetArmiesService;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.deletion.DeleteArmyService;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.persistance.PersistentArmyManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.persistance.SaveFileStrategy;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
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
        ArmyBuilderViewTest.ContextConfiguration.class,
        ArmyBuilderConfig.class,
        FXMLLoaderFactory.class,
        ArmyBuilderController.class,
        UnitDetailController.class,
        UnitListCellFactory.class,
        UnitListCellController.class,
        ApplicationState.class,
        SceneManagerConfig.class,
        RestTemplate.class,
        LocaleConfig.class,
        UnitPropertyController.class,
        AlertBuilder.class,
        SceneManager.class,
        GetArmiesService.class,
        ArmyAdapter.class,
        ArmyUnitAdapter.class,
        SaveFileStrategy.class,
        UserProvider.class
})
public class ArmyBuilderViewTest extends ApplicationTest {

    @MockBean
    private PersistentArmyManager persistentArmyManager;


    @TestConfiguration
    static class ContextConfiguration {
        @Bean
        public PersistentArmyManager persistentArmyManager() {return Mockito.mock(PersistentArmyManager.class);}

        @Bean
        public DeleteArmyService deleteArmyService() {return Mockito.mock(DeleteArmyService.class) ;}

        @Bean
        public DefaultArmyGenerator defaultArmyGenerator() {return  Mockito.mock(DefaultArmyGenerator.class); }

        @Bean
        public ArmyDetailController armyDetailController() { return Mockito.mock(ArmyDetailController.class);}

        @Bean
        public EditArmyController editArmyController() {
            return Mockito.mock(EditArmyController.class);
        }
    }

    @Autowired
    EditArmyController editArmyController;

    @Autowired
    public ObjectFactory<ViewComponent<ArmyBuilderController>> sceneBuilder;

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
        ViewComponent<ArmyBuilderController> armyBuilderScene= sceneBuilder.getObject();
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
        Army army = new Army();
        state.selectedArmy.set(army);

        Unit unit = new Unit();
        unit.name.set("Archer");
        unit.iconUrl.set(getClass().getResource("/assets/icons/army/magicDefense.png").toString());

        ViewComponent<ArmyBuilderController> armyBuilderComponent = sceneBuilder.getObject();

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

        clickOn("#editArmyButton");

        // Button is disabled
        Mockito.verify(editArmyController, Mockito.times(0)).setArmy(army);

        Platform.runLater(() -> stage.hide());
        WaitForAsyncUtils.waitForFxEvents();
    }
}