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
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.URL;
import java.util.*;

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

    public Button moveLeftButton;
    public Button moveRightButton;

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
        JavaFXUtils.setButtonIcons(
                moveLeftButton,
                getClass().getResource("/assets/icons/navigation/arrowBackWhite.png"),
                getClass().getResource("/assets/icons/navigation/arrowBackBlack.png"),
                80
        );
        JavaFXUtils.setButtonIcons(
                moveRightButton,
                getClass().getResource("/assets/icons/navigation/arrowForwardWhite.png"),
                getClass().getResource("/assets/icons/navigation/arrowForwardBlack.png"),
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
        final Map<Unit, SquadViewModel> squadMap = new HashMap<>();
        armySquadList.setItems(squadList);

        initializeSquadList(army, squadList, squadMap);
        addArmyUnitChangeListener(army, squadList, squadMap);
    }

    private void addArmyUnitChangeListener(Army army, ObservableList<SquadViewModel> squadList, Map<Unit, SquadViewModel> squadMap) {
        armyUnitChangeListener = change -> {
            boolean dirtyList = false;
            while (change.next()) {
                for (Unit unit : change.getRemoved()) {
                    final SquadViewModel squadViewModel = squadMap.get(unit);
                    squadViewModel.members.remove(unit);
                    if (squadViewModel.members.size() == 0) {
                        squadMap.remove(unit);
                        dirtyList = true;
                    }
                }
                for (Unit unit : change.getAddedSubList()) {
                    if (!squadMap.containsKey(unit)) {
                        squadMap.put(unit, new SquadViewModel());
                        dirtyList = true;
                    }
                    final SquadViewModel squadViewModel = squadMap.get(unit);
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
    public void moveUnitLeft(ActionEvent event){
        moveUnit(-1);
    }
    public void moveUnitRight(ActionEvent event){
        moveUnit(1);
    }
    public void moveUnit(int leftOrRight){
        //false is left true is right
        List<Unit> unitList = appState.selectedArmy.get().units;
        Unit selected = armyBuilderState.selectedUnit.get();
        Unit neighbour;
        for(int i=0; i<unitList.size();i++){
            if(unitList.get(i).equals(selected) && i>0){
                neighbour=unitList.get(i+leftOrRight);
                unitList.remove(i+leftOrRight);
                unitList.set(i+leftOrRight, selected);
                unitList.remove(i);
                unitList.set(i, neighbour);
            }
        }
    }
}
