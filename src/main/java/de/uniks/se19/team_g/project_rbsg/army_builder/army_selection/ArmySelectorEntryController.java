package de.uniks.se19.team_g.project_rbsg.army_builder.army_selection;

import de.uniks.se19.team_g.project_rbsg.model.Army;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class ArmySelectorEntryController extends ListCell<Army> implements Initializable {

    public ImageView imageView;

    @Override
    protected void updateItem(Army item, boolean empty) {
        super.updateItem(item, empty);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setText(null);
        setGraphic(null);
    }
}
