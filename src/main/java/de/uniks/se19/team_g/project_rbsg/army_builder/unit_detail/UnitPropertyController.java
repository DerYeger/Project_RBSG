package de.uniks.se19.team_g.project_rbsg.army_builder.unit_detail;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class UnitPropertyController {

    public Label propertyValue;
    public ImageView propertyIcon;


    public void bindTo(SimpleIntegerProperty prop, Image image) {
        if (prop == null) {
            propertyValue.setText("?");
        } else {
            propertyValue.textProperty().bind(prop.asString());
                    }

        propertyIcon.setImage(image);
    }
}
