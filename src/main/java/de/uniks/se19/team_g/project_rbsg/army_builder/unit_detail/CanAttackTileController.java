package de.uniks.se19.team_g.project_rbsg.army_builder.unit_detail;

import de.uniks.se19.team_g.project_rbsg.model.Unit;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@Scope("prototype")
public class CanAttackTileController implements Initializable {

    public Pane imagePane;
    private Unit unitDefinition;

    public void setDefinition(Unit unitDefinition) {
        this.unitDefinition = unitDefinition;
        setActive(false);
    }

    public void setActive(boolean active) {
        if (active) {
            imagePane.setStyle("-fx-background-image: url("+unitDefinition.iconUrl.get()+")");
        } else {
            imagePane.setStyle("-fx-background-color: transparent");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imagePane.prefWidthProperty().bind(imagePane.prefHeightProperty());
    }
}
