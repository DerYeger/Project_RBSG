package de.uniks.se19.team_g.project_rbsg.army_builder.army;

import de.uniks.se19.team_g.project_rbsg.army_builder.ArmyBuilderState;
import de.uniks.se19.team_g.project_rbsg.configuration.ApplicationState;
import de.uniks.se19.team_g.project_rbsg.model.Army;
import de.uniks.se19.team_g.project_rbsg.model.Unit;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

@Component
public class ArmyDetailController implements Initializable {

    public static final int ARMY_MAX_SIZE = 10;

    public Label armyNameLabel;
    public Label armySizeLabel;

    public ListView<SquadViewModel> armySquadList;

    @Nonnull
    private final ApplicationState appState;
    @Nonnull
    private final ArmyBuilderState armyBuilderState;
    @Nullable
    private final Callback<ListView<SquadViewModel>, ListCell<SquadViewModel>> cellFactory;
    private ChangeListener<Army> selectedArmyListener;

    // we want to hold a reference to our change listener so that we can add a weak list change listener and not get garbage collected to early
    @SuppressWarnings("FieldCanBeLocal")
    private ListChangeListener<? super Unit> armyUnitChangeListener;

    public ArmyDetailController(
        @Nonnull ApplicationState appState,
        @Nonnull ArmyBuilderState armyBuilderState,
        @Nullable ArmySquadCellFactory armySquadCellFactory
    ) {
        this.appState = appState;
        this.armyBuilderState = armyBuilderState;
        this.cellFactory = armySquadCellFactory;
        selectedArmyListener = this::onSelectedArmyUpdate;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        appState.selectedArmy.addListener(new WeakChangeListener<>(selectedArmyListener));
        final Army selectedArmy = appState.selectedArmy.get();
        if (selectedArmy != null) {
            bindToArmy(selectedArmy);
        }

        armySquadList.setCellFactory(cellFactory);
    }

    private void onSelectedArmyUpdate(ObservableValue<? extends Army> observableValue, Army prev, Army nextArmy) {
        bindToArmy(nextArmy);
    }

    private void bindToArmy(Army army) {
        armyNameLabel.textProperty().bind(army.name);

        armySizeLabel.textProperty().bind(
            Bindings.format(
                "%d/%d",
                Bindings.size(army.units),
                ARMY_MAX_SIZE
            )
        );
    }

    public void onAddUnit()
    {
        final Army army = appState.selectedArmy.get();
        final Unit unit = armyBuilderState.selectedUnit.get();
        if (Objects.nonNull(army) && Objects.nonNull(unit) && army.units.size() < ARMY_MAX_SIZE) {
            army.units.add(unit.clone());
        }
    }

    public void onRemoveUnit()
    {
        final Army army = appState.selectedArmy.get();
        final Unit unit = armyBuilderState.selectedUnit.get();
        if (Objects.nonNull(army) && Objects.nonNull(unit)) {
            army.units.remove(unit);
        }
    }

    private class Sqaud {
    }
}
