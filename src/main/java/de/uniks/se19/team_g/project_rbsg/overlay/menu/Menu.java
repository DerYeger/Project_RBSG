package de.uniks.se19.team_g.project_rbsg.overlay.menu;

import de.uniks.se19.team_g.project_rbsg.overlay.Overlay;
import de.uniks.se19.team_g.project_rbsg.util.Tuple;
import io.rincl.Rincled;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@Scope("prototype")
public class Menu extends Overlay implements Rincled {

    private List<Tuple<String, Node>> entries;

    @FXML
    private StackPane root;
    @FXML
    private VBox container;
    @FXML
    private Label label;

    public Menu setEntries(@NonNull final List<Tuple<String, Node>> entries) {
        this.entries = entries;
        return this;
    }

    @Override
    protected void init() {
        container.setSpacing(20);
        label.setText(text);
        for (final Tuple<String, Node> entry : entries) {
            final Label label = new Label(getResources().getString(entry.first));
            label.setAlignment(Pos.CENTER_LEFT);

            final Region filler = new Region();
            HBox.setHgrow(filler, Priority.ALWAYS);

            final HBox hBox = new HBox(label, filler, entry.second);
            hBox.setAlignment(Pos.CENTER);

            container.getChildren().add(hBox);
        }
        root.setOnMouseClicked(event -> hide());
    }
}
