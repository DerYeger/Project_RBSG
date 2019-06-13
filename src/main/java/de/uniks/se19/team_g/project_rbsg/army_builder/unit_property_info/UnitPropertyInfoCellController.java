package de.uniks.se19.team_g.project_rbsg.army_builder.unit_property_info;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class UnitPropertyInfoCellController {

    public ImageView imageView;
    public Label infoLabel;

    public void init() {
        imageView.setImage(new Image("/assets/icons/army/magic-defense.png"));
        infoLabel.setText("huhu");
    }

}
