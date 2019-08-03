package de.uniks.se19.team_g.project_rbsg.ingame.battlefield.unitInfo;

import de.uniks.se19.team_g.project_rbsg.configuration.flavor.*;
import de.uniks.se19.team_g.project_rbsg.ingame.model.*;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import org.slf4j.*;

import javax.annotation.*;
import java.net.*;
import java.util.*;

public class UnitInfoBoxController<T> implements Initializable
{
    public ImageView unitImageView;
    public VBox firstPropertyContainer;
    public VBox secondPropertyContainer;
    public HBox secondHorizontalContainer;
    public Pane playerColorPane;
    public Label hpLabel;

    private ChangeListener<T> unitChangeListener;
    private ChangeListener<Number> hpChangeListener;

    private StringProperty hpText = new SimpleStringProperty("No unit selected");
    private StringProperty mpText = new SimpleStringProperty("-");
    private StringProperty defaultText = new SimpleStringProperty("-");

    private int maxHp = 0;

    private PropertyInfoBuilder propertyInfoBuilder;
    private HashMap<UnitTypeInfo, Image> imageMap = new HashMap<>();


    private Logger logger = LoggerFactory.getLogger(getClass());

    public UnitInfoBoxController ()
    {
        propertyInfoBuilder = new PropertyInfoBuilder();
    }

    @Override
    public void initialize (URL location, ResourceBundle resources)
    {
        unitChangeListener = this::unitChanged;
        hpChangeListener = this::hpChanged;


        hpLabel.textProperty().bindBidirectional(hpText);

        firstPropertyContainer.getChildren().add(propertyInfoBuilder.build(getClass().getResource("/assets/icons/units/mpIcon.png").toExternalForm(), mpText));
        firstPropertyContainer.getChildren().add(propertyInfoBuilder.build(getClass().getResource("/assets/icons/units/unknownIcon.png").toExternalForm(), defaultText));
        secondPropertyContainer.getChildren().add(propertyInfoBuilder.build(getClass().getResource("/assets/icons/units/unknownIcon.png").toExternalForm(), defaultText));
        secondPropertyContainer.getChildren().add(propertyInfoBuilder.build(getClass().getResource("/assets/icons/units/unknownIcon.png").toExternalForm(), defaultText));


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
            image = new Image(unit.getUnitType().getImage().toExternalForm(), 100, 100, false, true);
            imageMap.put(unit.getUnitType(), image);
        }
        unitImageView.setImage(image);
        maxHp = unit.getMaxHp();
        unit.hpProperty().addListener(hpChangeListener);
        hpLabel.setGraphic(new ImageView(new Image(getClass().getResource("/assets/icons/units/hpIcon.png").toExternalForm(),
                                                   40,
                                                   40, false, true)));
        hpText.set(String.format("%d / %d ", unit.getHp(), maxHp));
        mpText.set(String.valueOf(unit.getMp()));
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
        hpText.set("No unit selected");
        hpLabel.setGraphic(null);
        mpText.set("-");
        playerColorPane.setBackground(new Background(new BackgroundFill(Color.rgb(255, 255, 255, 0.13),
                                                                        CornerRadii.EMPTY,
                                                                        Insets.EMPTY)));
    }

    public void bindUnit (@Nonnull final ObservableObjectValue<? extends T> unit)
    {
        unit.addListener(unitChangeListener);
    }


}
