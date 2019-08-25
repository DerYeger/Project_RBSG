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

    private HashMap<Army, HashMap<Unit, SquadViewModel>> armyMap;

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
        armyMap = new HashMap<>();
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
        final HashMap<Unit, SquadViewModel> squadMap = new HashMap<>();
        armySquadList.setItems(squadList);

        initializeSquadList(army, squadList, squadMap);
        addArmyUnitChangeListener(army, squadList, squadMap);

        armyMap.put(army, squadMap);
    }

    private void addArmyUnitChangeListener(Army army, ObservableList<SquadViewModel> squadList, Map<Unit, SquadViewModel> squadMap) {
        armyUnitChangeListener = change -> {
            while (change.next()) {
                for (Unit unit : change.getRemoved()) {
                    final SquadViewModel squadViewModel = squadMap.get(unit);
                    if(!change.wasReplaced()){

                        squadViewModel.members.remove(unit);
                        if (squadViewModel.members.size() == 0) {
                            squadMap.remove(unit);
                            squadList.remove(squadViewModel);
                        }
                    }
                }
                for (Unit unit : change.getAddedSubList()) {
                    if(!change.wasReplaced()){
                        SquadViewModel newModel = new SquadViewModel();
                        newModel.members.add(unit);
                        squadMap.put(unit, newModel);
                        squadList.add(newModel);
                    }
                }
            }
        };

        army.units.addListener(
            new WeakListChangeListener<>(armyUnitChangeListener)
        );
    }

    private void initializeSquadList(Army army, ObservableList<SquadViewModel> squadList, Map<Unit, SquadViewModel> squadMap) {
        for (Unit unit : army.units) {
            SquadViewModel newModel = new SquadViewModel();
            squadMap.put(unit, newModel);
            squadMap.get(unit).members.add(unit);
            squadList.add(newModel);
        }
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
        if(armySquadList.getSelectionModel().getSelectedIndex() == 0 && leftOrRight == -1 || armySquadList.getSelectionModel().getSelectedIndex() == 9 && leftOrRight == 1 || armySquadList.getSelectionModel().isEmpty()) return;
        armySquadList.getSelectionModel().select(armySquadList.getSelectionModel().getSelectedIndex()+leftOrRight);

        HashMap<Unit, SquadViewModel> squadMap = armyMap.get(appState.selectedArmy.get());
        ObservableList squadList = armySquadList.getItems();

        Unit selected = armyBuilderState.selectedUnit.get();
        SquadViewModel selectedModel = squadMap.get(selected);

        SquadViewModel neighbourModel;
        Unit neighbourUnit;
        for(int i=0; i<squadList.size();i++){

            if(squadList.get(i).equals(selectedModel)){

                if(i==0 && leftOrRight==-1 || i==squadList.size()-1 && leftOrRight==1){
                    return;
                }

                neighbourModel=(SquadViewModel) squadList.get(i+leftOrRight);
                neighbourUnit = neighbourModel.members.get(0);

                squadMap.remove(selected);
                squadMap.remove(neighbourUnit);

                moveListPositions(selected, neighbourUnit, leftOrRight);

                squadMap.put(neighbourUnit, selectedModel);
                squadMap.put(selected, neighbourModel);
                neighbourModel.members.set(0, selectedModel.members.set(0, neighbourUnit));

                armyBuilderState.selectedUnit.set(selected);
                armySquadList.refresh();

                appState.selectedArmy.get().setUnsavedUpdates(true);
                i=squadList.size();
            }
        }
    }

    private void moveListPositions(Unit selectedUnit, Unit neighbourUnit, int leftOrRight) {
        ObservableList<Unit> selectedArmyUnits = appState.selectedArmy.get().units;
        for(int i=0; i < selectedArmyUnits.size(); i++){
            if(selectedArmyUnits.get(i)==(selectedUnit)){
                Collections.swap(selectedArmyUnits, i, i+leftOrRight);
                i=selectedArmyUnits.size();
            }
        }
    }
}
