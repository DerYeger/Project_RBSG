package de.uniks.se19.team_g.project_rbsg.overlay.menu;

import de.uniks.se19.team_g.project_rbsg.overlay.Overlay;
import de.uniks.se19.team_g.project_rbsg.util.JavaFXUtils;
import de.uniks.se19.team_g.project_rbsg.util.Tuple;
import io.rincl.Rincled;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Locale;

@Controller
@Scope("prototype")
public class Menu extends Overlay implements Rincled {

    private List<Entry> entries;

    @FXML
    private StackPane root;
    @FXML
    private VBox container;
    @FXML
    private Label label;

    private Property<Locale> locale;

    public Menu setLocale(@NonNull final Property<Locale>  locale) {
        this.locale = locale;
        return this;
    }

    public Menu setEntries(@NonNull final List<Entry> entries) {
        this.entries = entries;
        return this;
    }

    @Override
    protected void init() {
        if (locale == null) locale = new SimpleObjectProperty<>(Locale.ENGLISH); //fallback
        container.setSpacing(20);
        label.textProperty().bind(JavaFXUtils.bindTranslation(locale, "menu"));
        for (final Entry entry : entries) {
            container.getChildren().add(entry.getEntry(locale));
        }
        root.setOnMouseClicked(event -> hide());
    }
}
