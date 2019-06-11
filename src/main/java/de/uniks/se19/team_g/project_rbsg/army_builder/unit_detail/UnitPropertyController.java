package de.uniks.se19.team_g.project_rbsg.army_builder.unit_detail;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.converter.NumberStringConverter;

public class UnitPropertyController {
    private NumberStringConverter converter = new NumberStringConverter();

    public Label propertyValue;
    public ImageView propertyIcon;


    public void bindTo(SimpleIntegerProperty prop, Image image) {
        propertyValue.textProperty().bindBidirectional(prop, converter);

        propertyIcon.setImage(image);
    }
}
