package de.uniks.se19.team_g.project_rbsg.ingame.battlefield.unitInfo;

import de.uniks.se19.team_g.project_rbsg.configuration.flavor.*;
import de.uniks.se19.team_g.project_rbsg.ingame.model.*;
import de.uniks.se19.team_g.project_rbsg.util.JavaFXUtils;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import org.slf4j.*;
import org.springframework.lang.NonNull;

import javax.annotation.*;
import java.net.*;
import java.util.*;

public class UnitInfoBoxController<T> implements Initializable
{
    private final String status;
    private final Property<Locale> selectedLocale;
    public ImageView unitImageView;
    public VBox firstPropertyContainer;
    public VBox secondPropertyContainer;
    public VBox thirdPropertyContainer;
    public HBox secondHorizontalContainer;
    public Pane playerColorPane;
    public Pane movementPane;
    public Label hpLabel;

    private ChangeListener<T> unitChangeListener;
    private ChangeListener<Number> hpChangeListener;

    private StringProperty hpText;
    private StringProperty mpText = new SimpleStringProperty("-");
    private StringProperty ca1Text = new SimpleStringProperty("-");
    private StringProperty ca2Text = new SimpleStringProperty("-");
    private StringProperty ca3Text = new SimpleStringProperty("-");
    private StringProperty ca4Text = new SimpleStringProperty("-");
    private StringProperty ca5Text = new SimpleStringProperty("-");
    private StringProperty ca6Text = new SimpleStringProperty("-");

    private int maxHp = 0;

    private PropertyInfoBuilder propertyInfoBuilder;
    private HashMap<UnitTypeInfo, Image> imageMap = new HashMap<>();

    public UnitInfoBoxController(String status, Property<Locale> selectedLocale)
    {
        this.selectedLocale = selectedLocale;
        this.status = status;
        hpText = new SimpleStringProperty("No unit " + status);
        propertyInfoBuilder = new PropertyInfoBuilder();
    }

    @Override
    public void initialize (URL location, ResourceBundle resources)
    {
        unitChangeListener = this::unitChanged;
        hpChangeListener = this::hpChanged;


        hpLabel.textProperty().bindBidirectional(hpText);

        movementPane.getChildren().add(propertyInfoBuilder.build(getClass().getResource("/assets/icons/units/mpIcon.png").toExternalForm(), mpText));
        firstPropertyContainer.getChildren().add(propertyInfoBuilder.build(UnitTypeInfo._INFANTRY.getImage().toExternalForm(), ca1Text));
        firstPropertyContainer.getChildren().add(propertyInfoBuilder.build(UnitTypeInfo._BAZOOKA_TROOPER.getImage().toExternalForm(), ca2Text));
        secondPropertyContainer.getChildren().add(propertyInfoBuilder.build(UnitTypeInfo._JEEP.getImage().toExternalForm(), ca3Text));
        secondPropertyContainer.getChildren().add(propertyInfoBuilder.build(UnitTypeInfo._LIGHT_TANK.getImage().toExternalForm(), ca4Text));
        thirdPropertyContainer.getChildren().add(propertyInfoBuilder.build(UnitTypeInfo._HEAVY_TANK.getImage().toExternalForm(), ca5Text));
        thirdPropertyContainer.getChildren().add(propertyInfoBuilder.build(UnitTypeInfo._CHOPPER.getImage().toExternalForm(), ca6Text));

        buildNullPreview();
    }

    private void unitChanged (ObservableValue<? extends T> observableValue, T oldHoverable, T newHoverable)
    {
        if (newHoverable instanceof Unit)
        {
            Unit newUnit = (Unit) newHoverable;
            buildPreview(newUnit);
        }
        else
        {
            buildNullPreview();
        }

        if (oldHoverable instanceof Unit)
        {
            Unit oldUnit = (Unit) oldHoverable;
            oldUnit.hpProperty().removeListener(hpChangeListener);
        }
    }


    private void hpChanged (ObservableValue<? extends Number> observableValue, Number oldHp, Number newHp)
    {
        hpText.unbind();
        hpText.set(String.format("%d / %d ", newHp.intValue(), maxHp));
    }

    private void buildPreview (Unit unit)
    {
        Image image;
        if (imageMap.containsKey(unit.getUnitType()))
        {
            image = imageMap.get(unit.getUnitType());
        }
        else
        {
            if(unit.getUnitType() != null) {
                image = new Image(unit.getUnitType().getImage().toExternalForm(), 100, 100,
                        false, true);
            }
            else {
                image = new Image(getClass().getResource("/assets/sprites/mr-unknown.png").toExternalForm(),
                        100, 100, false, true);
            }
            imageMap.put(unit.getUnitType(), image);
        }
        unitImageView.setImage(image);
        maxHp = unit.getMaxHp();
        unit.hpProperty().addListener(hpChangeListener);
        hpLabel.setGraphic(new ImageView(new Image(getClass().getResource("/assets/icons/units/hpIcon.png").toExternalForm(),
                                                   40,
                                                   40, false, true)));
        hpText.unbind();
        hpText.set(String.format("%d / %d ", unit.getHp(), maxHp));
        mpText.set(String.valueOf(unit.getMp()));
        ca1Text.set(String.valueOf(unit.getAttackValue(UnitTypeInfo._INFANTRY)));
        ca2Text.set(String.valueOf(unit.getAttackValue(UnitTypeInfo._BAZOOKA_TROOPER)));
        ca3Text.set(String.valueOf(unit.getAttackValue(UnitTypeInfo._JEEP)));
        ca4Text.set(String.valueOf(unit.getAttackValue(UnitTypeInfo._LIGHT_TANK)));
        ca5Text.set(String.valueOf(unit.getAttackValue(UnitTypeInfo._HEAVY_TANK)));
        ca6Text.set(String.valueOf(unit.getAttackValue(UnitTypeInfo._CHOPPER)));
        if(unit.getLeader() != null) {
            playerColorPane.setBackground(new Background(new BackgroundFill(Paint.valueOf(unit.getLeader().getColor()), CornerRadii.EMPTY, Insets.EMPTY)));
        }
        else {
            playerColorPane.setBackground(new Background(new BackgroundFill(Color.PINK, CornerRadii.EMPTY, Insets.EMPTY)));
        }
    }

    private void buildNullPreview ()
    {
        Image image = new Image(getClass().getResource("/assets/sprites/mr-unknown.png").toExternalForm(), 100, 100, false, true);
        unitImageView.setImage(image);
        hpText.bind(JavaFXUtils.bindTranslation(selectedLocale, status));
        hpLabel.setGraphic(null);
        mpText.set("-");
        ca1Text.set("-");
        ca2Text.set("-");
        ca3Text.set("-");
        ca4Text.set("-");
        ca5Text.set("-");
        ca6Text.set("-");
        playerColorPane.setBackground(new Background(new BackgroundFill(Color.rgb(255, 255, 255, 0.13),
                                                                        CornerRadii.EMPTY,
                                                                        Insets.EMPTY)));
    }

    public void bindUnit (@Nonnull final ObservableObjectValue<? extends T> unit)
    {
        unit.addListener(unitChangeListener);
    }


}
