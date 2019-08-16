package de.uniks.se19.team_g.project_rbsg.overlay.egg;

import de.uniks.se19.team_g.project_rbsg.overlay.Overlay;
import de.uniks.se19.team_g.project_rbsg.overlay.OverlayTarget;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import org.springframework.lang.NonNull;

public class EasterEgg extends Overlay {

    public EasterEgg(@NonNull final OverlayTarget overlayTarget,
                     @NonNull final Node node) {
        initialize(null, node, overlayTarget);
    }

    @Override
    protected void init() {
        final Node egg = node;
        final StackPane container = new StackPane(egg);
        container.setAlignment(Pos.CENTER);
//        container.setOnMouseClicked(event -> hide());
        container.toBack();
        container.autosize();
        node = container;
    }
}
