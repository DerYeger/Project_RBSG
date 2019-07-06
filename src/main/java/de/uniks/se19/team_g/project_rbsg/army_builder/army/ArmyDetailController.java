package de.uniks.se19.team_g.project_rbsg.army_builder.army;

import de.uniks.se19.team_g.project_rbsg.army_builder.ArmyBuilderState;
import de.uniks.se19.team_g.project_rbsg.configuration.ApplicationState;
import de.uniks.se19.team_g.project_rbsg.model.Army;
import de.uniks.se19.team_g.project_rbsg.model.Unit;
import de.uniks.se19.team_g.project_rbsg.util.JavaFXUtils;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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

    public Button incrementButton;
    public Button decrementButton;

    // we want to hold a reference to our change listener so that we can add a weak list change listener and not get garbage collected to early
    // @See ApplicationState
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
        JavaFXUtils.setButtonIcons(
                incrementButton,
                getClass().getResource("/assets/icons/operation/addWhite.png"),
                getClass().getResource("/assets/icons/operation/addBlack.png"),
                80
        );
        JavaFXUtils.setButtonIcons(
                decrementButton,
                getClass().getResource("/assets/icons/operation/removeWhite.png"),
                getClass().getResource("/assets/icons/operation/removeBlack.png"),
                80
        );
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
                ApplicationState.ARMY_MAX_UNIT_COUNT
            )
        );

        bindSquadList(army);


    }

    private void bindSquadList(Army army) {
        final ObservableList<SquadViewModel> squadList = FXCollections.observableArrayList();
        final Map<String, SquadViewModel> squadMap = new HashMap<>();
        armySquadList.setItems(squadList);

        initializeSquadList(army, squadList, squadMap);
        addArmyUnitChangeListener(army, squadList, squadMap);
    }

    private void addArmyUnitChangeListener(Army army, ObservableList<SquadViewModel> squadList, Map<String, SquadViewModel> squadMap) {
        armyUnitChangeListener = change -> {
            boolean dirtyList = false;
            while (change.next()) {
                for (Unit unit : change.getRemoved()) {
                    final String key = unit.id.get();
                    final SquadViewModel squadViewModel = squadMap.get(key);
                    squadViewModel.members.remove(unit);
                    if (squadViewModel.members.size() == 0) {
                        squadMap.remove(key);
                        dirtyList = true;
                    }
                }
                for (Unit unit : change.getAddedSubList()) {
                    final String key = unit.id.get();
                    if (!squadMap.containsKey(key)) {
                        squadMap.put(key, new SquadViewModel());
                        dirtyList = true;
                    }
                    final SquadViewModel squadViewModel = squadMap.get(key);
                    squadViewModel.members.add(unit);
                }
            }
            if (dirtyList) {
                squadList.setAll(squadMap.values());
            }
        };

        army.units.addListener(
            new WeakListChangeListener<>(armyUnitChangeListener)
        );
    }

    private void initializeSquadList(Army army, ObservableList<SquadViewModel> squadList, Map<String, SquadViewModel> squadMap) {
        for (Unit unit : army.units) {
            if (!squadMap.containsKey(unit.id.get())) {
                squadMap.put(unit.id.get(), new SquadViewModel());
            }
            squadMap.get(unit.id.get()).members.add(unit);
        }
        squadList.setAll(squadMap.values());
    }

    public void onAddUnit()
    {
        final Army army = appState.selectedArmy.get();
        final Unit unit = armyBuilderState.selectedUnit.get();
        if (Objects.nonNull(army) && Objects.nonNull(unit) && army.units.size() < ApplicationState.ARMY_MAX_UNIT_COUNT) {
            army.units.add(unit.clone());
            army.setUnsavedUpdates(true);
        }
    }

    public void onRemoveUnit()
    {
        final Army army = appState.selectedArmy.get();
        final Unit unit = armyBuilderState.selectedUnit.get();
        if (Objects.nonNull(army) && Objects.nonNull(unit)) {
            army.units.remove(unit);
            army.setUnsavedUpdates(true);
        }
    }
}
