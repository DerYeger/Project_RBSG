package de.uniks.se19.team_g.project_rbsg.army_builder.army;

import de.uniks.se19.team_g.project_rbsg.configuration.ApplicationState;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class ArmyDetailController implements Initializable {
    public Label armyNameLabel;
    public Label armySizeLabel;
    public ListView armySquadList;

    private final ApplicationState state;

    public ArmyDetailController(ApplicationState state) {
        this.state = state;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
