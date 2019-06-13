package de.uniks.se19.team_g.project_rbsg.configuration;

import javafx.beans.binding.Bindings;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author Keanu Stückrad
 */
public class ButtonIconsSetter {

    // Superclass used in most controllers

    public void setButtonIcons(Button button, String hoverIconName, String nonHoverIconName, int size) {
        ImageView hover = new ImageView();
        ImageView nonHover = new ImageView();
        nonHover.fitWidthProperty().setValue(size);
        nonHover.fitHeightProperty().setValue(size);
        hover.fitWidthProperty().setValue(size);
        hover.fitHeightProperty().setValue(size);
        hover.setImage(new Image(String.valueOf(getClass().getResource(hoverIconName))));
        nonHover.setImage(new Image(String.valueOf(getClass().getResource(nonHoverIconName))));
        button.graphicProperty().bind(Bindings.when(button.hoverProperty())
                .then(hover)
                .otherwise(nonHover));
    }

}