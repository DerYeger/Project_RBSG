package de.uniks.se19.team_g.project_rbsg.overlay.credits;

import de.uniks.se19.team_g.project_rbsg.overlay.Overlay;
import de.uniks.se19.team_g.project_rbsg.overlay.menu.Menu;
import de.uniks.se19.team_g.project_rbsg.util.JavaFXUtils;
import io.rincl.Rincled;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Label;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;

import java.util.Locale;

@Controller
public class Credits extends Overlay implements Rincled {

    public Label boardLabel;
    public Label iconLabel;
    public Label musicLabel;
    public Label unitLabel;
    public Label frameworkLabel;

    private Property<Locale> locale;

    public Credits setLocale(@NonNull final Property<Locale>  locale) {
        this.locale = locale;
        return this;
    }

    @Override
    protected void init() {
        if (locale == null) locale = new SimpleObjectProperty<>(Locale.ENGLISH); //fallback
        boardLabel.textProperty().bind(JavaFXUtils.bindTranslation(locale, "boardCredits"));
        iconLabel.textProperty().bind(JavaFXUtils.bindTranslation(locale, "iconCredits"));
        musicLabel.textProperty().bind(JavaFXUtils.bindTranslation(locale, "musicCredits"));
        unitLabel.textProperty().bind(JavaFXUtils.bindTranslation(locale, "unitCredits"));
        frameworkLabel.textProperty().bind(JavaFXUtils.bindTranslation(locale, "frameworkCredits"));
    }
}