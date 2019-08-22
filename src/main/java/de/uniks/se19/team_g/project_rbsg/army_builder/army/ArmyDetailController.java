package de.uniks.se19.team_g.project_rbsg.army_builder.army;

import de.uniks.se19.team_g.project_rbsg.army_builder.ArmyBuilderState;
import de.uniks.se19.team_g.project_rbsg.configuration.ApplicationState;
import de.uniks.se19.team_g.project_rbsg.model.Army;
import de.uniks.se19.team_g.project_rbsg.model.Unit;
import de.uniks.se19.team_g.project_rbsg.util.JavaFXUtils;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
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

        configureButtons();
    }

    protected void configureButtons(){
        SimpleBooleanProperty noArmiesLeft = new SimpleBooleanProperty(false);

        appState.armies.addListener((ListChangeListener<Army>) amryList -> {
            if (appState.armies.size() < 1){
                noArmiesLeft.set(true);
            } else {
                noArmiesLeft.set(false);
            }
        });

        SimpleBooleanProperty noArmyLeftOrSelected = new SimpleBooleanProperty(false);

        noArmyLeftOrSelected.bind(Bindings.createBooleanBinding(
                ()-> (noArmiesLeft.get() || appState.selectedArmy.get() == null),
                noArmiesLeft, appState.selectedArmy
        ));

        incrementButton.disableProperty().bind(noArmyLeftOrSelected);
        decrementButton.disableProperty().bind(noArmyLeftOrSelected);
        noArmiesLeft.addListener(((observable, oldValue, newValue) -> {}));
        noArmyLeftOrSelected.addListener(((observable, oldValue, newValue) -> {}));
        incrementButton.disableProperty().addListener(((observable, oldValue, newValue) -> {}));
        decrementButton.disableProperty().addListener(((observable, oldValue, newValue) -> {}));
    }

    private void onSelectedArmyUpdate(ObservableValue<? extends Army> observableValue, Army prev, Army nextArmy) {
        bindToArmy(nextArmy);
    }

    private void bindToArmy(@Nullable Army army) {
        if (army == null){
            return;
        }
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
        final Map<Unit, SquadViewModel> squadMap = new HashMap<>();
        armySquadList.setItems(squadList);

        initializeSquadList(army, squadList, squadMap);
        addArmyUnitChangeListener(army, squadList, squadMap);
    }

    private void addArmyUnitChangeListener(Army army, ObservableList<SquadViewModel> squadList, Map<Unit, SquadViewModel> squadMap) {
        armyUnitChangeListener = change -> {
            while (change.next()) {
                for (Unit unit : change.getRemoved()) {
                    final SquadViewModel squadViewModel = squadMap.get(unit);
                    squadViewModel.members.remove(unit);
                    if (squadViewModel.members.size() == 0) {
                        squadMap.remove(unit);
                        squadList.remove(squadViewModel);
                    }
                }
                for (Unit unit : change.getAddedSubList()) {
                        SquadViewModel newModel = new SquadViewModel();
                        newModel.members.add(unit);
                        squadMap.put(unit, newModel);
                        squadList.add(newModel);
                }
            }
        };

        army.units.addListener(
            new WeakListChangeListener<>(armyUnitChangeListener)
        );
    }

    private void initializeSquadList(Army army, ObservableList<SquadViewModel> squadList, Map<Unit, SquadViewModel> squadMap) {
        for (Unit unit : army.units) {
            squadMap.put(unit, new SquadViewModel());
            squadMap.get(unit).members.add(unit);
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
        armyBuilderState.selectedUnit.set(null);
    }
}
