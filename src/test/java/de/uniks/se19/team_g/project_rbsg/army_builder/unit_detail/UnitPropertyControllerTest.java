package de.uniks.se19.team_g.project_rbsg.army_builder.unit_detail;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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
        final SimpleIntegerProperty prop = new SimpleIntegerProperty(42);
        controller.bindTo(prop);

        WaitForAsyncUtils.asyncFx( () -> stage.setScene(new Scene(loader.getRoot())));
        WaitForAsyncUtils.waitForFxEvents();

        lookup("42").query();

        WaitForAsyncUtils.asyncFx(() -> prop.set(666));
        WaitForAsyncUtils.waitForFxEvents();


        Assert.assertEquals(0, lookup("42").queryAll().size());
        lookup("666").query();
    }

}