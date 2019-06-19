package de.uniks.se19.team_g.project_rbsg.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Army {

    public static final int ARMY_MAX_SIZE = 10;
    public final SimpleStringProperty id = new SimpleStringProperty();
    public final SimpleStringProperty name = new SimpleStringProperty();
    public final ObservableList<Unit> units = FXCollections.observableArrayList();
}
