package de.uniks.se19.team_g.project_rbsg.ingame.battlefield.unitInfo;

import de.uniks.se19.team_g.project_rbsg.ingame.model.*;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.fxml.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import org.slf4j.*;

import javax.annotation.*;
import java.net.*;
import java.util.*;

public class UnitInfoBoxController<T> implements Initializable
{
    public ImageView unitImageView;
    public VBox firstPropertyContainer;
    public VBox secondPropertyContainer;
    public VBox thirdPropertyContainer;

    private ChangeListener<T> unitChangeListener;
    private ChangeListener<Number> hpChangeListener;

    private StringProperty hpText = new SimpleStringProperty("No unit selected");
    private StringProperty mpText = new SimpleStringProperty("-");
    private StringProperty defaultText = new SimpleStringProperty("-");

    private PropertyInfoBuilder propertyInfoBuilder;

    private Logger logger = LoggerFactory.getLogger(getClass());

    public UnitInfoBoxController()
    {
        propertyInfoBuilder = new PropertyInfoBuilder();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        unitChangeListener = this::unitChanged;
        hpChangeListener = this::hpChanged;


        firstPropertyContainer.getChildren().add(propertyInfoBuilder.build(getClass().getResource("/assets/icons/units/hpIcon.png").toExternalForm(), hpText));
        secondPropertyContainer.getChildren().add(propertyInfoBuilder.build(getClass().getResource("/assets/icons/units/mpIcon.png").toExternalForm(), mpText));
        secondPropertyContainer.getChildren().add(propertyInfoBuilder.build(getClass().getResource("/assets/icons/units/unknownIcon.png").toExternalForm(), defaultText));
        thirdPropertyContainer.getChildren().add(propertyInfoBuilder.build(getClass().getResource("/assets/icons/units/unknownIcon.png").toExternalForm(), defaultText));
        thirdPropertyContainer.getChildren().add(propertyInfoBuilder.build(getClass().getResource("/assets/icons/units/unknownIcon.png").toExternalForm(), defaultText));
        thirdPropertyContainer.getChildren().add(propertyInfoBuilder.build(getClass().getResource("/assets/icons/units/unknownIcon.png").toExternalForm(), defaultText));

        buildNullPreview();
    }

    private void unitChanged(ObservableValue<? extends T> observableValue, T oldHoverable, T newHoverable)
    {
        logger.debug("Unit changed!");
        if(newHoverable instanceof Unit) {
            Unit newUnit = (Unit) newHoverable;
            buildPreview(newUnit);
        }
        else {
            buildNullPreview();
        }

        if(oldHoverable instanceof Unit) {
            Unit oldUnit = (Unit) oldHoverable;
            oldUnit.hpProperty().removeListener(hpChangeListener);
        }
    }


    private void hpChanged(ObservableValue<? extends Number> observableValue, Number oldHp, Number newHp)
    {
        hpText.set(String.format("%d / %d ", newHp.intValue(), 9001));
    }

    private void buildPreview(Unit unit)
    {
        Image image = new Image(unit.getUnitType().getImage().toExternalForm(), 100, 100, false, true);
        unitImageView.setImage(image);
        unit.hpProperty().addListener(hpChangeListener);
        hpText.set(String.format("%d / %d ", unit.getHp(), 9000));
        mpText.set(String.valueOf(unit.getMp()));
    }

    private void buildNullPreview()
    {
        Image image = new Image(getClass().getResource("/assets/sprites/mr-unknown.png").toExternalForm(), 100, 100, false, true);
        unitImageView.setImage(image);
        hpText.set("No unit selected");
        mpText.set("-");
    }

    public void bindUnit(@Nonnull final ObservableObjectValue<? extends T> unit)
    {
        unit.addListener(unitChangeListener);
    }


}
