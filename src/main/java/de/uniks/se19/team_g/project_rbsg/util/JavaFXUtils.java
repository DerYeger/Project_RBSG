package de.uniks.se19.team_g.project_rbsg.util;

import de.uniks.se19.team_g.project_rbsg.ProjectRbsgFXApplication;
import io.rincl.Rincl;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.value.ObservableStringValue;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.Locale;

public class JavaFXUtils {
    public static void setButtonIcons(Button button, URL defaultIconName, URL hoverIconName, int iconSize) {
        ImageView hover = new ImageView();
        ImageView nonHover = new ImageView();

        nonHover.fitWidthProperty().setValue(iconSize);
        nonHover.fitHeightProperty().setValue(iconSize);

        hover.fitWidthProperty().setValue(iconSize);
        hover.fitHeightProperty().setValue(iconSize);

        hover.setImage(new Image(hoverIconName.toString()));
        nonHover.setImage(new Image(defaultIconName.toString()));

        button.graphicProperty().bind(Bindings.when(button.hoverProperty())
                .then(hover)
                .otherwise(nonHover));
    }

    public static void bindImage(ObjectProperty<Image> imageProperty, ObservableStringValue imgUrlProperty) {
        final ObjectBinding<Image> imageBinding = Bindings.createObjectBinding(() -> new Image(imgUrlProperty.get()), imgUrlProperty);
        imageProperty.bind(imageBinding);

    }

    public static ObservableValue<? extends String> bindTranslation(Property<Locale> selectedLocale, String key) {
        return Bindings.createStringBinding(
                () -> Rincl.getResources(ProjectRbsgFXApplication.class).getString(key),
                selectedLocale
        );
    }
}