package de.uniks.se19.team_g.project_rbsg.ingame.battlefield.unitInfo;

import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.TileUtils;
import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.uiModel.Tile;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;
import de.uniks.se19.team_g.project_rbsg.termination.Terminable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class UnitInfoBoxController implements Initializable, Terminable
{
    public ImageView unitImageView;
    public VBox firstPropertyContainer;
    public VBox secondPropertyContainer;
    public VBox thirdPropertyContainer;

    private ChangeListener<Unit> unitChangeListener;
    private ChangeListener<Number> hpChangeListener;

    private ObjectProperty<Unit> unit;
    private StringProperty hpText;
    private StringProperty mpText;

    private PropertyInfoBuilder propertyInfoBuilder;

    public UnitInfoBoxController()
    {
        propertyInfoBuilder = new PropertyInfoBuilder();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        unit = new SimpleObjectProperty<>();
        hpText = new SimpleStringProperty(null);
        unitChangeListener = this::unitChanged;
        hpChangeListener = this::hpCHanged;
        unit.addListener(unitChangeListener);


        firstPropertyContainer.getChildren().add(propertyInfoBuilder.build(getClass().getResource("/assets/icons/units/hpIcon.png").toExternalForm(), hpText));
        secondPropertyContainer.getChildren().add(propertyInfoBuilder.build(getClass().getResource("/assets/icons/units/mpIcon.png").toExternalForm(), mpText));
        secondPropertyContainer.getChildren().add(propertyInfoBuilder.build(null, null));
        thirdPropertyContainer.getChildren().add(propertyInfoBuilder.build(null, null));
        thirdPropertyContainer.getChildren().add(propertyInfoBuilder.build(null, null));
        thirdPropertyContainer.getChildren().add(propertyInfoBuilder.build(null, null));
    }

    private void hpCHanged(ObservableValue<? extends Number> observableValue, Number oldHp, Number newHp)
    {
        hpText.set(String.format("%d / %d ", 9000, newHp.intValue()));
    }

    private void unitChanged(ObservableValue<? extends Unit> observableValue, Unit oldUnit, Unit newUnit)
    {
        if(newUnit != null) {
            Image image = new Image(TileUtils.getUnitImagePath(unit.get().getUnitType()));
            unitImageView.setImage(image);
            unit.get().hpProperty().addListener(hpChangeListener);
            mpText.set(String.valueOf(unit.get().getMp()));
        }
        else {
            //TODO: if unit is unset
        }

        if(oldUnit != null) {
            oldUnit.hpProperty().removeListener(hpChangeListener);
        }
    }

    public void bindUnit(ObjectProperty<Unit> unit)
    {
        this.unit.bind(unit);
    }

    @Override
    public void terminate()
    {
        unit.removeListener(unitChangeListener);
    }
}
