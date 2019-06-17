package de.uniks.se19.team_g.project_rbsg.army_builder.army;

import de.uniks.se19.team_g.project_rbsg.ViewComponent;
import de.uniks.se19.team_g.project_rbsg.army_builder.ArmyBuilderConfig;
import de.uniks.se19.team_g.project_rbsg.configuration.ApplicationState;
import de.uniks.se19.team_g.project_rbsg.configuration.FXMLLoaderFactory;
import de.uniks.se19.team_g.project_rbsg.model.Army;
import de.uniks.se19.team_g.project_rbsg.model.Unit;
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

    @Override
    public void start(Stage stage) {

        final Army army = new Army();
        final Unit unit1 = Unit.unknownType("1");
        final Unit unit2 = Unit.unknownType("2");
        final Unit unit3 = Unit.unknownType("3");
        army.units.setAll(unit1, unit2, unit3);

        state.armies.setAll(army);

        final HBox root = new HBox();
        armyDetailMounter.apply(root);

        stage.setScene(new Scene(root, 800, 500));
        stage.show();
    }

    @Test
    public void test()
    {
        Assert.assertEquals(6, lookup(".list-cell .label").queryAll().size());
    }
}