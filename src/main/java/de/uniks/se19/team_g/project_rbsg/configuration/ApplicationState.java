package de.uniks.se19.team_g.project_rbsg.configuration;

import de.uniks.se19.team_g.project_rbsg.model.Army;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Component;

@Component
public class ApplicationState {

    public final SimpleObjectProperty<Army> selectedArmy = new SimpleObjectProperty<>();
    public final ObservableList<Army> armies =  FXCollections.observableArrayList();
}
