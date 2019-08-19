package de.uniks.se19.team_g.project_rbsg.overlay.menu;

import de.uniks.se19.team_g.project_rbsg.util.JavaFXUtils;
import javafx.beans.property.Property;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import java.util.Locale;

public class Entry {

    private static final Logger LOGGER = LoggerFactory.getLogger(Entry.class);

    private final String translationKey;
    private final Node node;
    private final Orientation orientation;

    public Entry(@NonNull final String translationKey,
                 @NonNull final Node node,
                 @NonNull final Orientation orientation) {
        this.translationKey = translationKey;
        this.node = node;
        this.orientation = orientation;
    }

    public Node getEntry(@NonNull final Property<Locale> selectedLocale) {
        if (orientation.equals(Orientation.HORIZONTAL)) {
            return asHorizontal(selectedLocale);
        } else if (orientation.equals(Orientation.VERTICAL)) {
            return asVertical(selectedLocale);
        } else {
            LOGGER.error("Unsupported orientation");
            return null;
        }
    }

    private Node asHorizontal(@NonNull final Property<Locale> selectedLocale) {
        final Label label = new Label();
        label.textProperty().bind(JavaFXUtils.bindTranslation(selectedLocale, translationKey));
        label.setAlignment(Pos.CENTER_LEFT);

        final Region filler = new Region();
        HBox.setHgrow(filler, Priority.ALWAYS);

        final HBox hBox = new HBox(label, filler, node);
        hBox.setAlignment(Pos.CENTER);

        return hBox;
    }

    private Node asVertical(@NonNull final Property<Locale> selectedLocale) {
        final Label label = new Label();
        label.textProperty().bind(JavaFXUtils.bindTranslation(selectedLocale, translationKey));
        label.setAlignment(Pos.CENTER);

        final VBox vBox = new VBox(label, node);
        vBox.setAlignment(Pos.CENTER);

        return vBox;
    }
}
