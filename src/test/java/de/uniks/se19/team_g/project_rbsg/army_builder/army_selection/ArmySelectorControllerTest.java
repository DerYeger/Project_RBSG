package de.uniks.se19.team_g.project_rbsg.army_builder.army_selection;

import de.uniks.se19.team_g.project_rbsg.configuration.FXMLLoaderFactory;
import de.uniks.se19.team_g.project_rbsg.configuration.LocaleConfig;
import de.uniks.se19.team_g.project_rbsg.model.Army;
import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
    ArmySelectorConfig.class,
    ArmySelectorCellFactory.class,
    FXMLLoaderFactory.class,
    ArmySelectorController.class,
    ArmySelectorCellController.class,
    LocaleConfig.class
})
public class ArmySelectorControllerTest extends ApplicationTest {


    @Autowired
    private Function<VBox, ArmySelectorController> armySelectionMounter;

    private VBox mountPoint;

    @Override
    public void start(Stage stage) {

        mountPoint = new VBox();
        final Pane root = new Pane(mountPoint);

        stage.setScene(new Scene(root, 200, 500));
        stage.show();
    }

    @Test
    public void test() throws ExecutionException, InterruptedException
    {
        final ArmySelectorController controller = CompletableFuture.supplyAsync(
            () -> armySelectionMounter.apply(mountPoint),
            Platform::runLater
        ).get();

        final Army army1 = new Army();
        final Army army2 = new Army();
        final Army army3 = new Army();
        final ObservableList<Army> armies = FXCollections.observableArrayList(army1, army2, army3);

        Property<Army> selected =  new SimpleObjectProperty<>();
        Platform.runLater(() -> controller.setSelection(armies, selected));
        WaitForAsyncUtils.waitForFxEvents();

        Assert.assertEquals(3, lookup(".list-cell #imageView").queryAll().size());

        clickOn(lookup(".list-cell #imageView").nth(1).<Node>query());

        Assert.assertSame(army2, selected.getValue());

    }
}