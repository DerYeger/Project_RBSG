package de.uniks.se19.team_g.project_rbsg.configuration;

import de.uniks.se19.team_g.project_rbsg.model.Army;
import de.uniks.se19.team_g.project_rbsg.model.Unit;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import javafx.beans.value.ObservableBooleanValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Component;

@Component
public class ApplicationState {

    public final SimpleObjectProperty<Army> selectedArmy = new SimpleObjectProperty<>();
    public final ObservableList<Army> armies =  FXCollections.observableArrayList();
    public final ObservableList<Unit> unitDefinitions = FXCollections.observableArrayList();
    public final SimpleBooleanProperty validArmySelected = new SimpleBooleanProperty();

    public ApplicationState ()
    {
        setupValidArmySelected();

        armies.addListener(this::onArmyUpdate);

    }

    protected void setupValidArmySelected() {
        validArmySelected.set(false);
        selectedArmy.addListener((observable, oldValue, newValue) ->{
            if (newValue == null) {
                validArmySelected.unbind();
                validArmySelected.setValue(false);
            } else {
                final Army army = selectedArmy.get();
                final BooleanBinding validArmySizeBinding = new SimpleListProperty<>(army.units).sizeProperty().isEqualTo(Army.ARMY_MAX_SIZE);

                validArmySelected.bind(
                        army.id.isNotNull().and(validArmySizeBinding)
                );
            }
        });
    }

    private void onArmyUpdate(ListChangeListener.Change<? extends Army> change) {
        while (change.next()) {
            if (selectedArmy.get() == null || change.getRemoved().contains(selectedArmy.get())) {
                selectedArmy.set(change.getList().get(0));
                if (selectedArmy.get() != null) {
                    break;
                }
            }
        }
    }
}