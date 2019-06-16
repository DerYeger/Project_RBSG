package de.uniks.se19.team_g.project_rbsg.army_builder.army;

import de.uniks.se19.team_g.project_rbsg.army_builder.ArmyBuilderState;
import de.uniks.se19.team_g.project_rbsg.model.Unit;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@Scope("prototype")
public class ArmySquadController extends ListCell<Unit> implements Initializable {

    public Label typeLabel;
    public Label countLabel;
    public Parent root;

    private Unit unit;

    private final ArmyBuilderState armyBuilderState;

    public ArmySquadController(ArmyBuilderState armyBuilderState) {
        this.armyBuilderState = armyBuilderState;
    }

    @Override
    protected void updateItem(final Unit unit, final boolean empty) {
        this.unit = unit;
        super.updateItem(unit, empty);

        if (unit == null || empty) {
            setText(null);
            setGraphic(null);
            return;
        }

        typeLabel.textProperty().bind(unit.name);
        countLabel.setText("1");

        setGraphic(root);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setText(null);
        setGraphic(null);

        root.setOnMouseClicked(this::onSelection);
    }

    private void onSelection(MouseEvent mouseEvent) {
        System.out.println("clicked on " + unit.id.get());
        armyBuilderState.selectedUnit.set(unit);
    }
}
