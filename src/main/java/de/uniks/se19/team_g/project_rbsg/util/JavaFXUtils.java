package de.uniks.se19.team_g.project_rbsg.util;

import de.uniks.se19.team_g.project_rbsg.ProjectRbsgFXApplication;
import io.rincl.Rincl;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.*;
import javafx.beans.value.ObservableStringValue;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import javax.annotation.Nonnull;
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

    public static void setButtonIcons(Button button, URL trueIconName, URL falseIconName, int iconSize,
                                      BooleanProperty condition) {
        ImageView trueImage = new ImageView();
        ImageView falseImage = new ImageView();

        falseImage.fitWidthProperty().setValue(iconSize);
        falseImage.fitHeightProperty().setValue(iconSize);

        trueImage.fitWidthProperty().setValue(iconSize);
        trueImage.fitHeightProperty().setValue(iconSize);

        trueImage.setImage(new Image(trueIconName.toString()));
        falseImage.setImage(new Image(falseIconName.toString()));

        button.graphicProperty().bind(Bindings.when(condition)
                                              .then(trueImage)
                                              .otherwise(falseImage));
    }

    public static void bindImage(ObjectProperty<Image> imageProperty, ObservableStringValue imgUrlProperty) {
        final ObjectBinding<Image> imageBinding = Bindings.createObjectBinding(() -> new Image(imgUrlProperty.get()), imgUrlProperty);
        imageProperty.bind(imageBinding);

    }

    public static void bindImage(ObjectProperty<Image> imageProperty, ObjectProperty<Image> observableImage) {
        final ObjectBinding<Image> imageBinding = Bindings.createObjectBinding(() -> observableImage.get(),
                                                                               observableImage);
        imageProperty.bind(imageBinding);

    }


    public static void bindButtonDisableWithTooltip(Button button, Pane container, StringProperty tooltip, BooleanProperty flag)
    {
        BooleanBinding tooltipBinding = Bindings.createBooleanBinding(
                () -> {
                    if (flag.get()) return false;

                    Tooltip.install(container, createInvalidArmyTooltip(tooltip));
                    return true;
                },
                flag
        );

        button.disableProperty().bind(tooltipBinding);
    }

    @Nonnull
    protected static Tooltip createInvalidArmyTooltip(StringProperty tooltipText) {
        final Tooltip tooltip = new Tooltip();
        tooltip.textProperty().bind(tooltipText);
        tooltip.setShowDelay(Duration.millis(500));
        return tooltip;
    }

    public static ObservableValue<? extends String> bindTranslation(Property<Locale> selectedLocale, String key) {
        return Bindings.createStringBinding(
                () -> Rincl.getResources(ProjectRbsgFXApplication.class).getString(key),
                selectedLocale
        );
    }
}