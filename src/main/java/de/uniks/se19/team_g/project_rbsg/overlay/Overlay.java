package de.uniks.se19.team_g.project_rbsg.overlay;

import javafx.application.Platform;
import javafx.scene.Node;
import org.springframework.lang.NonNull;

public abstract class Overlay {

    protected String text;
    protected Node node;
    protected OverlayTarget target;

    public void initialize(@NonNull final String text,
                           @NonNull final Node node,
                           @NonNull final OverlayTarget target) {
        this.text = text;
        this.node = node;
        this.target = target;
        init();
    }

    protected abstract void init();

    public Node getNode() {
        return node;
    }

    public void show() {
        target.showOverlay(this);
    }

    public void hide() {
        target.hideOverlay(this);
    }
}
