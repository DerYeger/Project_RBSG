package de.uniks.se19.team_g.project_rbsg.army_builder;

import de.uniks.se19.team_g.project_rbsg.ViewComponent;
import de.uniks.se19.team_g.project_rbsg.army_builder.unit_detail.UnitDetailController;
import de.uniks.se19.team_g.project_rbsg.configuration.FXMLLoaderFactory;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Parent;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testfx.framework.junit.ApplicationTest;

/**
 * @author Goatfryed
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        ArmyBuilderConfig.class,
        FXMLLoaderFactory.class,
        SceneController.class,
        UnitDetailController.class
})
public class ArmyBuilderSceneTest extends ApplicationTest {

    @Autowired
    public ApplicationContext context;

    @Test
    public void testSceneCreation()
    {
        @SuppressWarnings("unchecked") ViewComponent<SceneController> armyBuilderScene
                = (ViewComponent<SceneController>) context.getBean("armyBuilderScene");
        final SceneController controller = armyBuilderScene.getController();

        Assert.assertNotNull(controller.armyBuilderScene);
        Assert.assertNotNull(controller.content);
        Assert.assertNotNull(controller.sideBarLeft);
        Assert.assertNotNull(controller.sideBarRight);
        Assert.assertNotNull(controller.unitDetailView);
        Assert.assertNotNull(controller.armyView);
        Assert.assertNotNull(controller.unitListView);
    }
}