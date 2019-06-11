package de.uniks.se19.team_g.project_rbsg.army_builder.unit_detail;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.io.IOException;

public class UnitPropertyControllerTest extends ApplicationTest {

    private Stage stage;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        stage.show();
    }

    @Test
    public void test() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(UnitPropertyController.class.getResource("/ui/army_builder/UnitPropertyView.fxml"));
        loader.load();
        final UnitPropertyController controller = loader.getController();
        final SimpleIntegerProperty prop = new SimpleIntegerProperty(666);
        controller.bindTo(
            prop,
            new Image(
                getClass().getResource("/assets/icons/army/magic-defense.png").toString()
            )
        );

        WaitForAsyncUtils.asyncFx( () -> stage.setScene(new Scene(loader.getRoot())));
        WaitForAsyncUtils.waitForFxEvents();

        lookup("666").query();

        WaitForAsyncUtils.asyncFx(() -> prop.set(42));
        WaitForAsyncUtils.waitForFxEvents();


        Assert.assertEquals(0, lookup("666").queryAll().size());
        lookup("42").query();
    }

}