package de.uniks.se19.team_g.project_rbsg.army_builder.army;

import de.uniks.se19.team_g.project_rbsg.ViewComponent;
import de.uniks.se19.team_g.project_rbsg.army_builder.ArmyBuilderConfig;
import de.uniks.se19.team_g.project_rbsg.army_builder.ArmyBuilderState;
import de.uniks.se19.team_g.project_rbsg.configuration.ApplicationState;
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
import org.testfx.robot.Motion;
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

    public static final int BASE_X = 150;
    public static final int BASE_Y = 50;

    @Autowired
    private Function<HBox,ViewComponent<ArmyDetailController>> armyDetailMounter;

    @Autowired
    private ApplicationState state;

    @Autowired
    private ArmyBuilderState armyBuilderState;

    private Army selectedArmy;

    @Override
    public void start(Stage stage) {

        selectedArmy = new Army();
        selectedArmy.name.set( "My awesome Army");

        state.selectedArmy.set(selectedArmy);

        final HBox root = new HBox();
        armyDetailMounter.apply(root);

        stage.setScene(new Scene(root, 800, 500));
        stage.setX(BASE_X);
        stage.setY(BASE_Y);
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

    @Test
    public void testMoving(){
        final Unit unit1 = Unit.unknownType("1");
        final Unit unit2 = Unit.unknownType("2");
        final Unit unit3 = Unit.unknownType("3");
        Platform.runLater(() -> selectedArmy.units.setAll(unit1, unit2, unit3));
        WaitForAsyncUtils.waitForFxEvents();

        //click on second unit
        click(170,250);
        sleep(1000);
        clickOn("#moveLeftButton");
        Assert.assertEquals(state.selectedArmy.get().units.get(0), unit2);

        click(70,250);
        clickOn("#moveLeftButton");
        Assert.assertEquals(state.selectedArmy.get().units.get(0), unit2);

        click(170,250);
        clickOn("#moveRightButton");
        Assert.assertEquals(state.selectedArmy.get().units.get(2), unit1);

    }

    private void click(double x, double y) {
        clickOn(BASE_X + x, BASE_Y + y, Motion.DIRECT);
    }
}