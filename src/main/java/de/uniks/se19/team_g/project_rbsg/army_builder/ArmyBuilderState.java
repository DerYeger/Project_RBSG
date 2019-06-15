package de.uniks.se19.team_g.project_rbsg.army_builder;

import de.uniks.se19.team_g.project_rbsg.model.Unit;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ArmyBuilderState {

    public final SimpleObjectProperty<Unit> selectedUnit = new SimpleObjectProperty<>();
    public  ObservableList<Unit> unitTypes = FXCollections.observableArrayList();
}
