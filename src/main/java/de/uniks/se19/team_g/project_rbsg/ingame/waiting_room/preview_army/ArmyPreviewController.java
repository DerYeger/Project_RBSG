package de.uniks.se19.team_g.project_rbsg.ingame.waiting_room.preview_army;

import de.uniks.se19.team_g.project_rbsg.model.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;



public class ArmyPreviewController
{
    @FXML
    public FlowPane armyContainer;
    @FXML
    public Label armyNameLabel;

    public void init(Army army) {
        String armyName = army.name.getName();
        
        if( armyName == null || armyName.isEmpty() || armyName.isBlank()) {
            armyName = "Your Army";
        }

        armyNameLabel.setText(armyName);

        for (Unit unit : army.units)
        {
            ImageView unitView = new ImageView(new Image(unit.getTypeInfo().getImage().toExternalForm(), 75, 75, false, true));
            armyContainer.getChildren().add(unitView);
        }
    }
}
