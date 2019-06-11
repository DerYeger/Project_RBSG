package de.uniks.se19.team_g.project_rbsg.army_builder.unit_detail;

import de.uniks.se19.team_g.project_rbsg.army_builder.ArmyBuilderConfig;
import de.uniks.se19.team_g.project_rbsg.configuration.FXMLLoaderFactory;
import de.uniks.se19.team_g.project_rbsg.model.Unit;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        FXMLLoaderFactory.class,
        UnitDetailController.class,
        UnitPropertyController.class,
        ArmyBuilderConfig.class,
})
public class UnitDetailControllerTest extends ApplicationTest {

    private Stage stage;

    @Autowired
    private FXMLLoaderFactory loaderFactory;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        stage.show();
    }

    @Test
    public void testBind() throws IOException {
        Unit unit1 = new Unit();
        unit1.imageUrl.set( getClass().getResource("/assets/sprites/Soldier.png").toString());
        unit1.health.set(20);
        final int physicalResistance = 15;
        unit1.physicalResistance.set(physicalResistance);
        unit1.magicResistance.set(5);
        unit1.speed.set(34);
        unit1.attack.set(100);
        unit1.spellPower.set(2);
        final String unit1Description = "/chuckMe";
        unit1.description.set(unit1Description);
        Unit unit2 = new Unit();
        unit2.imageUrl.set( getClass().getResource("/assets/sprites/Soldier.png").toString());
        unit2.health.set(11);
        unit2.physicalResistance.set(21);
        unit2.magicResistance.set(31);
        unit2.speed.set(41);
        unit2.attack.set(51);
        unit2.spellPower.set(61);
        unit2.description.set("Chuck Norris wins the game");

        final FXMLLoader loader = loaderFactory.fxmlLoader();
        loader.setLocation(getClass().getResource("/ui/army_builder/UnitDetailView.fxml"));
        loader.load();
        final UnitDetailController controller = loader.getController();
        controller.bind(unit1);

        Platform.runLater(() -> stage.setScene(new Scene(loader.getRoot())));
        WaitForAsyncUtils.waitForFxEvents();

        Assert.assertEquals(
                String.valueOf(unit1.health.get()),
                lookup(".unitPropertyContainer").nth(0).lookup(".label").<Label>query().getText()
        );
        Assert.assertEquals(
                String.valueOf(unit1.physicalResistance.get()),
                lookup(".unitPropertyContainer").nth(1).lookup(".label").<Label>query().getText()
        );
        Assert.assertEquals(
                String.valueOf(unit1.magicResistance.get()),
                lookup(".unitPropertyContainer").nth(2).lookup(".label").<Label>query().getText()
        );
        Assert.assertEquals(
                String.valueOf(unit1.speed.get()),
                lookup(".unitPropertyContainer").nth(3).lookup(".label").<Label>query().getText()
        );
        Assert.assertEquals(
                String.valueOf(unit1.attack.get()),
                lookup(".unitPropertyContainer").nth(4).lookup(".label").<Label>query().getText()
        );
        Assert.assertEquals(
                String.valueOf(unit1.spellPower.get()),
                lookup(".unitPropertyContainer").nth(5).lookup(".label").<Label>query().getText()
        );
        Assert.assertEquals(
                unit1.description.get(),
                lookup(".unitPropertyContainer.text-area").<TextArea>query().getText()
        );

        Platform.runLater(() -> controller.bind(unit2));
        WaitForAsyncUtils.waitForFxEvents();


        Assert.assertEquals(
                String.valueOf(unit2.physicalResistance.get()),
                lookup(".unitPropertyContainer").nth(1).lookup(".label").<Label>query().getText()
        );
        Assert.assertEquals(
                unit2.description.get(),
                lookup(".unitPropertyContainer.text-area").<TextArea>query().getText()
        );

        final String noCake = "there is no cake";
        Platform.runLater(() -> {
            unit1.imageUrl.set("something invalid");
            unit2.description.set(noCake);
            unit1.physicalResistance.set(999);
        });
        WaitForAsyncUtils.waitForFxEvents();

        Assert.assertEquals(
                String.valueOf(unit2.physicalResistance.get()),
                lookup(".unitPropertyContainer").nth(1).lookup(".label").<Label>query().getText()
        );
        Assert.assertEquals(
                noCake,
                lookup(".unitPropertyContainer.text-area").<TextArea>query().getText()
        );
    }
}