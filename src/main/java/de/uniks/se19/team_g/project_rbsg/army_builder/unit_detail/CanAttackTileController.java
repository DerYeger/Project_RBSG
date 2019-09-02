package de.uniks.se19.team_g.project_rbsg.army_builder.unit_detail;

import de.uniks.se19.team_g.project_rbsg.model.Unit;
import de.uniks.se19.team_g.project_rbsg.util.AttackCalculator;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@Scope("prototype")
public class CanAttackTileController implements Initializable {

    public Pane imagePane;
    public Label attackVal;
    private Unit unitDefinition;
    private SimpleObjectProperty<Unit> selectedUnit;

    public void setDefinition(Unit unitDefinition, SimpleObjectProperty<Unit> selectedUnit) {
        this.selectedUnit = selectedUnit;
        this.unitDefinition = unitDefinition;
        setActive(false);
    }

    public void setActive(boolean active) {
        if (active) {
            attackVal.setText(String.valueOf(AttackCalculator.getAttackValue(selectedUnit.get(), unitDefinition)));
            imagePane.setStyle("-fx-background-image: url("+unitDefinition.iconUrl.get()+")");
            Platform.runLater(() -> attackVal.setVisible(true));
        } else {
            imagePane.setStyle("-fx-background-color: transparent");
            Platform.runLater(() -> attackVal.setVisible(false));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imagePane.prefWidthProperty().bind(imagePane.prefHeightProperty());
    }
}
