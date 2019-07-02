package de.uniks.se19.team_g.project_rbsg.model;

import de.uniks.se19.team_g.project_rbsg.configuration.ApplicationState;
import de.uniks.se19.team_g.project_rbsg.configuration.ArmyIcon;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Army {

    public final SimpleStringProperty id = new SimpleStringProperty();
    public final SimpleStringProperty name = new SimpleStringProperty();
    public final ObservableList<Unit> units = FXCollections.observableArrayList();
    public final SimpleListProperty<Unit> simpleUnits = new SimpleListProperty<>(units);
    public final ReadOnlyBooleanProperty isPlayable;

    private final BooleanProperty hasUnsavedUpdates = new SimpleBooleanProperty();

    public final SimpleObjectProperty<ArmyIcon> iconType = new SimpleObjectProperty<>(ArmyIcon.DRAGON_HEAD);


    public Army() {
        SimpleBooleanProperty isPlayable = new SimpleBooleanProperty();
        isPlayable.bind(id.isNotNull().and(simpleUnits.sizeProperty().isEqualTo(ApplicationState.ARMY_MAX_UNIT_COUNT)));
        this.isPlayable = isPlayable;
    }

    public ReadOnlyBooleanProperty hasUnsavedUpdatesProperty() {
        return hasUnsavedUpdates;
    }

    public boolean hasUnsavedUpdates() {
        return hasUnsavedUpdates.get();
    }

    public void setHasUnsavedUpdates(boolean hasUnsavedUpdates) {
        this.hasUnsavedUpdates.set(hasUnsavedUpdates);
    }
}
