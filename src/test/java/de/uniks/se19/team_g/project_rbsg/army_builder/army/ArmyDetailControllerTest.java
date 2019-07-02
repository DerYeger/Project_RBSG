package de.uniks.se19.team_g.project_rbsg.army_builder.army;

import de.uniks.se19.team_g.project_rbsg.ViewComponent;
import de.uniks.se19.team_g.project_rbsg.army_builder.ArmyBuilderConfig;
import de.uniks.se19.team_g.project_rbsg.configuration.ApplicationState;
import de.uniks.se19.team_g.project_rbsg.configuration.ArmyIcon;
import de.uniks.se19.team_g.project_rbsg.configuration.FXMLLoaderFactory;
import de.uniks.se19.team_g.project_rbsg.model.Army;
import de.uniks.se19.team_g.project_rbsg.model.Unit;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.util.Set;
import java.util.function.Function;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        ApplicationState.class,
        ArmyBuilderConfig.class,
        FXMLLoaderFactory.class,
        ArmyDetailController.class,
        ArmySquadCellFactory.class,
        ArmySquadController.class
})
public class ArmyDetailControllerTest extends ApplicationTest {

    @Autowired
    private Function<HBox,ViewComponent<ArmyDetailController>> armyDetailMounter;

    @Autowired
    private ApplicationState state;

    private Army selectedArmy;

    /**
     * it appears, the ArmyIcon class must be used before the start method is called for some thread concurrency reasons
     */
    private ArmyIcon placeholder;

    @Override
    public void start(Stage stage) {

        selectedArmy = new Army();
        selectedArmy.name.set( "My awesome Army");

        state.selectedArmy.set(selectedArmy);

        final HBox root = new HBox();
        armyDetailMounter.apply(root);

        stage.setScene(new Scene(root, 800, 500));
        stage.show();
    }

    @Test
    public void test()
    {
        final Unit unit1 = Unit.unknownType("1");
        final Unit unit2 = Unit.unknownType("2");
        final Unit unit3 = Unit.unknownType("3");
        Platform.runLater(() -> selectedArmy.units.setAll(unit1, unit2, unit3));
        WaitForAsyncUtils.waitForFxEvents();


        final Set<Node> nodes = lookup(".list-cell #imageView").queryAll();
        Assert.assertEquals(3, nodes.size());
    }
}