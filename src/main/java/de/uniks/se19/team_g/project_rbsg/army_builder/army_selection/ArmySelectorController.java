package de.uniks.se19.team_g.project_rbsg.army_builder.army_selection;

import de.uniks.se19.team_g.project_rbsg.model.Army;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class ArmySelectorController implements Initializable {

    public ListView<Army> listView;

    private final ArmySelectorCellFactory cellFactory;

    public ArmySelectorController(ArmySelectorCellFactory cellFactory) {
        this.cellFactory = cellFactory;
    }

    public void setSelection(ObservableList<Army> armies)
    {
        listView.setItems(armies);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listView.setCellFactory(cellFactory);
    }
}
