package de.uniks.se19.team_g.project_rbsg.configuration;

import de.uniks.se19.team_g.project_rbsg.model.Army;
import de.uniks.se19.team_g.project_rbsg.model.Unit;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Component;

@Component
public class ApplicationState {

    public static final int MAX_ARMY_COUNT = 7;

    public final SimpleObjectProperty<Army> selectedArmy = new SimpleObjectProperty<>();
    public final ObservableList<Army> armies =  FXCollections.observableArrayList();
    public final ObservableList<Unit> unitDefinitions = FXCollections.observableArrayList();

    public ApplicationState () {
        armies.addListener(this::onArmyUpdate);
    }

    private void onArmyUpdate(ListChangeListener.Change<? extends Army> change) {
        change.next();
        if (selectedArmy.get() == null || change.getRemoved().contains(selectedArmy.get())) {
            selectedArmy.set(change.getList().get(0));
        }
    }
}