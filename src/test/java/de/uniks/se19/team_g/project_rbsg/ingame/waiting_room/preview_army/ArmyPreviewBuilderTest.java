package de.uniks.se19.team_g.project_rbsg.ingame.waiting_room.preview_army;

import de.uniks.se19.team_g.project_rbsg.configuration.flavor.UnitTypeInfo;
import de.uniks.se19.team_g.project_rbsg.model.Army;
import de.uniks.se19.team_g.project_rbsg.model.Unit;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class ArmyPreviewBuilderTest extends ApplicationTest
{

    public Stage stage;

    private ArmyPreviewController controller;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
    }


    @Test
    public void testNodeCreation() {
        ArmyPreviewBuilder armyPreviewBuilder = new ArmyPreviewBuilder();
        Army army = generateArmy();

        Node armyPreviewNode = armyPreviewBuilder.build(army);
        controller = armyPreviewBuilder.getLastController();

        assertThat(armyPreviewNode, notNullValue());
        assertThat(controller, notNullValue());


        assertThat(controller.armyContainer, notNullValue());
        assertThat(controller.armyNameLabel, notNullValue());

    }

    @Test
    public void testArmyPreview() {
        ArmyPreviewBuilder armyPreviewBuilder = new ArmyPreviewBuilder();
        Army army = generateArmy();

        Node armyPreviewNode = armyPreviewBuilder.build(army);
        controller = armyPreviewBuilder.getLastController();

        HBox root = new HBox();
        Platform.runLater(() -> {

            stage.setScene(new Scene(root));
            root.getChildren().add(armyPreviewNode);
            stage.show();
        });
        WaitForAsyncUtils.waitForFxEvents();
    }

    private Army generateArmy()
    {
        Army army = new Army();

        Unit unitOne = new Unit();
        unitOne.setTypeInfo(UnitTypeInfo._BAZOOKA_TROOPER);
        Unit unitTwo = new Unit();
        unitOne.setTypeInfo(UnitTypeInfo._CHOPPER);
        Unit unitThree = new Unit();
        unitOne.setTypeInfo(UnitTypeInfo._HEAVY_TANK);
        Unit unitFour = new Unit();
        unitOne.setTypeInfo(UnitTypeInfo._INFANTRY);
        Unit unitFive = new Unit();
        unitOne.setTypeInfo(UnitTypeInfo._JEEP);
        Unit unitSix = new Unit();
        unitOne.setTypeInfo(UnitTypeInfo._LIGHT_TANK);
        Unit unitSeven = new Unit();
        unitOne.setTypeInfo(UnitTypeInfo.UNKNOWN);

        army.units.addAll(unitOne, unitTwo, unitThree, unitFour, unitFive, unitSix, unitSeven);

        army.name.set("Rouge Squadron");

        return army;
    }

    @Test
    public void ArmyPreviewTest() {

    }
}
