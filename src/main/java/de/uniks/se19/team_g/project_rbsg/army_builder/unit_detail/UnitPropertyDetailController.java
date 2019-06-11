package de.uniks.se19.team_g.project_rbsg.army_builder.unit_detail;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.converter.NumberStringConverter;

public class UnitPropertyDetailController {
    private NumberStringConverter converter = new NumberStringConverter();

    public Label propertyValue;
    public VBox propertyIcon;


    public void bindTo(SimpleIntegerProperty prop) {
        propertyValue.textProperty().bindBidirectional(prop, converter);
    }
}
