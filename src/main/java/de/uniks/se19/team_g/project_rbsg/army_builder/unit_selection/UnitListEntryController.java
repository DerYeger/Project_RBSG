package de.uniks.se19.team_g.project_rbsg.army_builder.unit_selection;

import de.uniks.se19.team_g.project_rbsg.army_builder.ArmyBuilderState;
import de.uniks.se19.team_g.project_rbsg.model.Unit;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@Scope("prototype")
public class UnitListEntryController extends ListCell<Unit> implements Initializable {
    public static final String SELECTED_CLASS = "selected";
    private final ChangeListener<Unit> unitChangeListener = this::onSelectedUnitChanged;
    public Label unitName;
    public ImageView imageView;
    public Parent root;

    private Unit unit;

    private final ArmyBuilderState armyBuilderState;

    public UnitListEntryController(ArmyBuilderState armyBuilderState) {
        this.armyBuilderState = armyBuilderState;
    }

    @Override
    protected void updateItem(final Unit unit, final boolean empty) {
        this.unit = unit;
        super.updateItem(unit, empty);

        if (unit == null || empty) {
            setText(null);
            setGraphic(null);
            return;
        }

        unitName.textProperty().bind(unit.name);

        final ObjectBinding<Image> imageBinding = Bindings.createObjectBinding(() -> new Image(unit.iconUrl.get()), unit.iconUrl);
        imageView.imageProperty().bind(imageBinding);

        setGraphic(root);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setText(null);
        setGraphic(null);

        root.setOnMouseClicked(this::onSelection);

        armyBuilderState.selectedUnit.addListener(new WeakChangeListener<>(unitChangeListener));
    }

    private void onSelectedUnitChanged(ObservableValue<? extends Unit> observableValue, Unit prev, Unit next) {
        if (unit != null && unit == next) {
            root.getStyleClass().add(SELECTED_CLASS);
        } else {
            root.getStyleClass().remove(SELECTED_CLASS);
        }
    }

    private void onSelection(MouseEvent mouseEvent) {
        armyBuilderState.selectedUnit.set(unit);
    }
}
