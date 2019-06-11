package de.uniks.se19.team_g.project_rbsg.army_builder;

import de.uniks.se19.team_g.project_rbsg.model.Army;
import de.uniks.se19.team_g.project_rbsg.model.Unit;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class State {

    public final ObservableList<Army> armies =  FXCollections.observableArrayList();
    public final SimpleObjectProperty<Army> selectedArmy = new SimpleObjectProperty<>();
    public final SimpleObjectProperty<Unit> selectedUnit = new SimpleObjectProperty<>();
}
