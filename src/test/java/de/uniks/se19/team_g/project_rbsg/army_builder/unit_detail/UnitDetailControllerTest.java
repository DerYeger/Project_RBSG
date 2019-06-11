package de.uniks.se19.team_g.project_rbsg.army_builder.unit_detail;

import de.uniks.se19.team_g.project_rbsg.army_builder.ArmyBuilderConfig;
import de.uniks.se19.team_g.project_rbsg.configuration.FXMLLoaderFactory;
import de.uniks.se19.team_g.project_rbsg.model.Unit;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
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
        Unit unit = new Unit();
        unit.imageUrl.set( getClass().getResource("/assets/sprites/Soldier.png").toString());
        unit.health.set(20);
        unit.physicalResistance.set(15);
        unit.magicResistance.set(5);
        unit.speed.set(34);
        unit.attack.set(100);
        unit.spellPower.set(2);
        unit.description.set(
                "Brave soldier who fought in many battles. he is your standard unit." +
                "He has melee range and can overcome himself in battle." +
                "Special ability: A normal attack has the chance to do a critical hit"
        );

        final FXMLLoader loader = loaderFactory.fxmlLoader();
        loader.setLocation(getClass().getResource("/ui/army_builder/UnitDetailView.fxml"));
        loader.load();
        ((UnitDetailController) loader.getController()).bind(unit);

        Platform.runLater(() -> {
            stage.setScene(new Scene(loader.getRoot()));
        });
        WaitForAsyncUtils.waitForFxEvents();

    }
}