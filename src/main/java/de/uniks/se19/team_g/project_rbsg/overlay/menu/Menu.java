package de.uniks.se19.team_g.project_rbsg.overlay.menu;

import de.uniks.se19.team_g.project_rbsg.overlay.Overlay;
import de.uniks.se19.team_g.project_rbsg.util.Tuple;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@Scope("prototype")
public class Menu extends Overlay {

    private List<Tuple<String, Node>> entries;

    @FXML
    private StackPane root;
    @FXML
    private VBox container;
    @FXML
    private Label label;

    public Menu withEntries(@NonNull final List<Tuple<String, Node>> entries) {
        this.entries = entries;

        return this;
    }

    @Override
    protected void init() {
        label.setText(text);
        for (final Tuple<String, Node> entry : entries) {
            final HBox hBox = new HBox(new Label(entry.first), entry.second);
            hBox.setAlignment(Pos.CENTER);
            container.getChildren().add(hBox);
        }
        root.setOnMouseClicked(event -> hide());
    }
}
