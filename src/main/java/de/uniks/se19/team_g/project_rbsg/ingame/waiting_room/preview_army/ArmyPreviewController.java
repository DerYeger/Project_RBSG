package de.uniks.se19.team_g.project_rbsg.ingame.waiting_room.preview_army;

import de.uniks.se19.team_g.project_rbsg.model.*;
import javafx.fxml.*;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;


public class ArmyPreviewController
{
    @FXML
    public FlowPane armyContainer;
    @FXML
    public Label armyNameLabel;

    public void init(Army army) {
        String armyName = army.name.getName();
        
        if( armyName == null || armyName.isEmpty() || armyName.isBlank()) {
            armyName = "Cool Runnings";
        }

        armyNameLabel.setText(armyName);

        for (Unit unit : army.units)
        {

            Pane unitPane = new Pane();
            ImageView unitView = new ImageView(new Image(unit.getTypeInfo().getImage().toExternalForm(), 75, 75, false, true));
            unitPane.getChildren().add(unitView);
            unitPane.setBackground(new Background(new BackgroundFill(Color.rgb(255, 255, 255, 0.07),
                    CornerRadii.EMPTY, Insets.EMPTY)));
            armyContainer.getChildren().add(unitPane);

        }
    }
}
