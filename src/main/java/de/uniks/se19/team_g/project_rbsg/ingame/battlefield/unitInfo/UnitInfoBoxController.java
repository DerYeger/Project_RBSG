package de.uniks.se19.team_g.project_rbsg.ingame.battlefield.unitInfo;

import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.*;
import de.uniks.se19.team_g.project_rbsg.ingame.model.*;
import de.uniks.se19.team_g.project_rbsg.termination.*;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.fxml.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import org.slf4j.*;

import javax.annotation.*;
import java.net.*;
import java.util.*;

public class UnitInfoBoxController implements Initializable, Terminable
{
    public ImageView unitImageView;
    public VBox firstPropertyContainer;
    public VBox secondPropertyContainer;
    public VBox thirdPropertyContainer;

    private ChangeListener<Unit> unitChangeListener;
    private ChangeListener<Number> hpChangeListener;

    private ObjectProperty<Unit> unit = new SimpleObjectProperty<>();
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
        unit.addListener(unitChangeListener);


        firstPropertyContainer.getChildren().add(propertyInfoBuilder.build(getClass().getResource("/assets/icons/units/hpIcon.png").toExternalForm(), hpText));
        secondPropertyContainer.getChildren().add(propertyInfoBuilder.build(getClass().getResource("/assets/icons/units/mpIcon.png").toExternalForm(), mpText));
        secondPropertyContainer.getChildren().add(propertyInfoBuilder.build(getClass().getResource("/assets/icons/units/unknownIcon.png").toExternalForm(), defaultText));
        thirdPropertyContainer.getChildren().add(propertyInfoBuilder.build(getClass().getResource("/assets/icons/units/unknownIcon.png").toExternalForm(), defaultText));
        thirdPropertyContainer.getChildren().add(propertyInfoBuilder.build(getClass().getResource("/assets/icons/units/unknownIcon.png").toExternalForm(), defaultText));
        thirdPropertyContainer.getChildren().add(propertyInfoBuilder.build(getClass().getResource("/assets/icons/units/unknownIcon.png").toExternalForm(), defaultText));
    }

    private void hpChanged(ObservableValue<? extends Number> observableValue, Number oldHp, Number newHp)
    {
        hpText.set(String.format("%d / %d ", newHp.intValue(), 9000));
    }

    private void unitChanged(ObservableValue<? extends Unit> observableValue, Unit oldUnit, Unit newUnit)
    {
        logger.debug("Unit changed!");
        if (newUnit != null)
        {
            Image image = new Image(TileUtils.getUnitImagePath(unit.get().getUnitType()));
            unitImageView.setImage(image);
            unit.get().hpProperty().addListener(hpChangeListener);
            hpText.set(String.format("%d / %d ", unit.get().getHp(), 9000));
            mpText.set(String.valueOf(unit.get().getMp()));
        }
        else
        {
            Image image = new Image(getClass().getResource("/assets/sprites/mr-unknown.png").toExternalForm());
            unitImageView.setImage(image);
            hpText.set("No unit selected");
            mpText.set("-");
        }

        if (oldUnit != null)
        {
            oldUnit.hpProperty().removeListener(hpChangeListener);
        }
    }

    public void bindUnit(@Nonnull final ReadOnlyObjectProperty<Unit> unit)
    {
        this.unit.bind(unit);
    }

    @Override
    public void terminate()
    {
        unit.removeListener(unitChangeListener);
    }
}
