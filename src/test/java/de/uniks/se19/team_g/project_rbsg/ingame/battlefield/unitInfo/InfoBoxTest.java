package de.uniks.se19.team_g.project_rbsg.ingame.battlefield.unitInfo;

import de.uniks.se19.team_g.project_rbsg.configuration.flavor.UnitTypeInfo;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;
import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.util.Locale;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class InfoBoxTest extends ApplicationTest
{

    private Stage stage;

    private UnitInfoBoxController<Unit> controller;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
    }

    @Test
    public void testNodeCreation() {
        UnitInfoBoxBuilder<Unit> builder = new UnitInfoBoxBuilder<>();
        Unit unit = new Unit("0");

        SimpleObjectProperty<Unit> unitProperty = new SimpleObjectProperty<>(unit);

        Property<Locale> selectedLocale = new SimpleObjectProperty<>();
        selectedLocale.setValue(Locale.ENGLISH);
        Node infoBox = builder.build(unitProperty, selectedLocale);
        UnitInfoBoxController<Unit> controller = builder.getLastController();

        assertThat(infoBox, notNullValue());
        assertThat(controller, notNullValue());


        assertThat(controller.firstPropertyContainer, notNullValue());
        assertThat(controller.hpLabel, notNullValue());
        assertThat(controller.playerColorPane, notNullValue());
        assertThat(controller.secondHorizontalContainer, notNullValue());
        assertThat(controller.unitImageView, notNullValue());
    }

    @Test
    public void testUnitInfoView() {
        UnitInfoBoxBuilder<Unit> builder = new UnitInfoBoxBuilder<>();
        Unit unit = new Unit("0");
        unit.setUnitType(UnitTypeInfo._BAZOOKA_TROOPER);
        unit.setMaxHp(10);
        unit.setHp(5);
        unit.setMp(20);

        Player player = new Player("0");
        player.setColor("Blue");
        unit.setLeader(player);

        SimpleObjectProperty<Unit> unitProperty = new SimpleObjectProperty<>(null);

        Property<Locale> selectedLocale = new SimpleObjectProperty<>();
        selectedLocale.setValue(Locale.ENGLISH);
        Node infoBox = builder.build(unitProperty, selectedLocale);
        UnitInfoBoxController<Unit> controller = builder.getLastController();

        HBox root = new HBox();
        Platform.runLater(() -> {

            stage.setScene(new Scene(root));
            root.getChildren().add(infoBox);
            stage.show();
        });
        WaitForAsyncUtils.waitForFxEvents();

        assertThat(controller.hpLabel.getText(), is("No unit selected"));
        assertThat(controller.playerColorPane.getBackground().getFills().get(0).getFill(), is(Color.rgb(255, 255, 255
                , 0.13)));

        Platform.runLater(() -> {
            unitProperty.set(unit);
        });
        WaitForAsyncUtils.waitForFxEvents();

        assertThat(controller.hpLabel.getText(), is("5 / 10 "));
        assertThat(controller.playerColorPane.getBackground().getFills().get(0).getFill(), is(Paint.valueOf("Blue")));
    }

}
