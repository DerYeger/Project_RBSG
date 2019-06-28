package de.uniks.se19.team_g.project_rbsg.model;

import de.uniks.se19.team_g.project_rbsg.configuration.ApplicationState;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Army {

    public final SimpleStringProperty id = new SimpleStringProperty();
    public final SimpleStringProperty name = new SimpleStringProperty();
    public final ObservableList<Unit> units = FXCollections.observableArrayList();
    public final SimpleListProperty<Unit> simpleUnits = new SimpleListProperty<>(units);
    public final ReadOnlyBooleanProperty isPlayable;


    public Army() {
        SimpleBooleanProperty isPlayable = new SimpleBooleanProperty();
        isPlayable.bind(id.isNotNull().and(simpleUnits.sizeProperty().isEqualTo(ApplicationState.ARMY_MAX_UNIT_COUNT)));
        this.isPlayable = isPlayable;
    }
}
