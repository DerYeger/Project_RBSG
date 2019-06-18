package de.uniks.se19.team_g.project_rbsg.army_builder.army_selection;

import de.uniks.se19.team_g.project_rbsg.model.Army;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

public class ArmySelectorController {

    public ListView<Army> listView;

    public void setSelection(ObservableList<Army> armies)
    {
        listView.setItems(armies);
    }
}
