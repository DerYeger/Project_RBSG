package de.uniks.se19.team_g.project_rbsg.army_builder.army;

import de.uniks.se19.team_g.project_rbsg.configuration.ApplicationState;
import de.uniks.se19.team_g.project_rbsg.model.Army;
import de.uniks.se19.team_g.project_rbsg.model.Unit;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class ArmyDetailController implements Initializable {

    public Label armyNameLabel;
    public Label armySizeLabel;

    public ListView<Unit> armySquadList;

    private final ApplicationState state;
    private ChangeListener<Army> selectedArmyListener;

    public ArmyDetailController(ApplicationState state) {
        this.state = state;
        selectedArmyListener = this::onSelectedArmyUpdate;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        state.selectedArmy.addListener(new WeakChangeListener<>(selectedArmyListener));
        final Army selectedArmy = state.selectedArmy.get();
        if (selectedArmy != null) {
            bindToArmy(selectedArmy);
        }
    }

    private void onSelectedArmyUpdate(ObservableValue<? extends Army> observableValue, Army prev, Army nextArmy) {
        bindToArmy(nextArmy);
    }

    private void bindToArmy(Army army) {
        armyNameLabel.textProperty().bind(army.name);
        armySquadList.setItems(army.units);
    }
}
