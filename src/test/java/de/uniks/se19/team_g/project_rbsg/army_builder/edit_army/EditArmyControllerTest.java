package de.uniks.se19.team_g.project_rbsg.army_builder.edit_army;

import de.uniks.se19.team_g.project_rbsg.ViewComponent;
import de.uniks.se19.team_g.project_rbsg.army_builder.ArmyBuilderConfig;
import de.uniks.se19.team_g.project_rbsg.configuration.ArmyIcon;
import de.uniks.se19.team_g.project_rbsg.configuration.FXMLLoaderFactory;
import de.uniks.se19.team_g.project_rbsg.configuration.LocaleConfig;
import de.uniks.se19.team_g.project_rbsg.model.Army;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        EditArmyController.class,
        IconCellController.class,
        IconCellFactory.class,
        ArmyBuilderConfig.class,
        FXMLLoaderFactory.class,
        LocaleConfig.class,
})
public class EditArmyControllerTest extends ApplicationTest {

    @Autowired
    ObjectFactory<ViewComponent<EditArmyController>> editArmyComponent;

    @Test
    public void test() throws ExecutionException, InterruptedException {

        ViewComponent<EditArmyController> sut = editArmyComponent.getObject();

        ArmyIcon initialIcon = ArmyIcon.values()[0];
        ArmyIcon nextIcon = ArmyIcon.values()[1];

        Army army = new Army();
        army.name.set("Cool army");
        army.iconType.set(initialIcon);

        WaitForAsyncUtils.asyncFx(() -> {
            Stage nextStage = new Stage();
            nextStage.setScene(new Scene(sut.getRoot()));
            nextStage.show();
            sut.getController().setArmy(army);
        }).get();

        AtomicBoolean onCloseWasCalled = new AtomicBoolean(false);

        sut.getController().setOnClose(
            () -> onCloseWasCalled.set(true)
        );

        Assert.assertEquals(
            "Cool army",
            lookup(".text-input").<TextField>query().getText()
        );
        Assert.assertEquals(
            army.iconType.get().getImage(),
            lookup("#selectedIcon").<ImageView>query().getImage()
        );

        Assert.assertEquals(
            ArmyIcon.values().length,
            lookup(".list-cell #imageView").queryAll().size()
        );

        final Node nextIconCell = lookup(".list-cell #imageView").nth(1).query();

        clickOn(nextIconCell);

        Assert.assertEquals(
            initialIcon,
            army.iconType.get()
        );
        Assert.assertEquals(
            nextIcon.getImage(),
            lookup("#selectedIcon").<ImageView>query().getImage()
        );

        clickOn("#cancelButton");
        Assert.assertTrue(onCloseWasCalled.get());

        Assert.assertEquals(
                initialIcon.getImage(),
                lookup("#selectedIcon").<ImageView>query().getImage()
        );
        Assert.assertEquals(
                initialIcon,
                army.iconType.get()
        );

        clickOn(nextIconCell);

        onCloseWasCalled.set(false);
        clickOn("#submitButton");
        Assert.assertTrue(onCloseWasCalled.get());

        Assert.assertEquals(
                nextIcon,
                army.iconType.get()
        );
    }
}