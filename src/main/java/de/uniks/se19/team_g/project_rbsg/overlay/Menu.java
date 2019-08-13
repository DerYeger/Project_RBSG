package de.uniks.se19.team_g.project_rbsg.overlay;

import de.uniks.se19.team_g.project_rbsg.util.Tuple;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@Scope("prototype")
public class Menu extends Overlay {

    private List<Tuple<String, Node>> entries;

    public Menu withEntries(@NonNull final List<Tuple<String, Node>> entries) {
        this.entries = entries;
        for (final Tuple<String, Node> entry : entries) {
            final HBox hBox = new HBox(new Label(entry.first), entry.second);
            ((VBox) node).getChildren().add(hBox);
        }
        return this;
    }

    @Override
    protected void init() {

    }
}
