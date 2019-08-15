package de.uniks.se19.team_g.project_rbsg.army_builder.unit_property_info;

import io.rincl.Rincl;
import io.rincl.Rincled;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.Locale;

/**
 * @author  Keanu Stückrad
 */
public class UnitPropertyInfoCellController implements Rincled {

    public ImageView imageView;
    public Label infoLabel;

    public void init(Property<Locale> selectedLocale, Image propertyImage, String propertyInfo) {
        imageView.setImage(propertyImage);
        if(selectedLocale != null) infoLabel.textProperty().bind(
                Bindings.createStringBinding(() -> Rincl.getResources(UnitPropertyInfoCellController.class).getString(propertyInfo),
                        selectedLocale
                )
        );
    }

}
