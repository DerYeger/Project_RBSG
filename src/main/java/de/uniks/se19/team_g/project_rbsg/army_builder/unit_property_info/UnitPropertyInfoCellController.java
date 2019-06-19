package de.uniks.se19.team_g.project_rbsg.army_builder.unit_property_info;

import io.rincl.Rincled;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author  Keanu Stückrad
 */
public class UnitPropertyInfoCellController implements Rincled {

    public ImageView imageView;
    public Label infoLabel;
    private String propertyInfo;

    public void init(Image propertyImage, String propertyInfo) {
        imageView.setImage(propertyImage);
        this.propertyInfo = propertyInfo;
        updateLabels();
    }

    public void updateLabels(){
        infoLabel.textProperty().setValue(getResources().getString(propertyInfo));
        // More Properties
    }

}
