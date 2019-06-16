package de.uniks.se19.team_g.project_rbsg.army_builder.army;

import de.uniks.se19.team_g.project_rbsg.army_builder.ArmyBuilderState;
import de.uniks.se19.team_g.project_rbsg.configuration.ApplicationState;
import de.uniks.se19.team_g.project_rbsg.model.Army;
import de.uniks.se19.team_g.project_rbsg.model.Unit;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

@Component
public class ArmyDetailController implements Initializable {

    public Label armyNameLabel;
    public Label armySizeLabel;

    public ListView<Unit> armySquadList;

    @Nonnull
    private final ApplicationState appState;
    @Nonnull
    private final ArmyBuilderState armyBuilderState;
    @Nullable
    private final Callback<ListView<Unit>, ListCell<Unit>> cellFactory;
    private ChangeListener<Army> selectedArmyListener;

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
        armySquadList.setItems(army.units);
    }

    public void onAddUnit()
    {
        final Army army = appState.selectedArmy.get();
        final Unit unit = armyBuilderState.selectedUnit.get();
        if (Objects.nonNull(army) && Objects.nonNull(unit)) {
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
}
