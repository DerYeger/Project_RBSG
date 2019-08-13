package de.uniks.se19.team_g.project_rbsg.overlay;

import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import org.springframework.lang.NonNull;

import java.util.ArrayList;

public class OverlayTarget extends StackPane {

    public static OverlayTarget of(@NonNull final Parent parent) {
        return new OverlayTarget(parent);
    }

    private int maxCurrentOverlays = 1;
    private ArrayList<Overlay> overlays = new ArrayList<>();

    protected OverlayTarget(@NonNull final Parent parent) {
        super(parent);
    }

    public OverlayTarget showOverlay(@NonNull final Overlay overlay) {
        Platform.runLater(() -> {
            if (overlays.size() < maxCurrentOverlays) {
                overlays.add(overlay);
                getChildren().add(overlay.getNode());
            }
        });
        return this;
    }

    public OverlayTarget hideOverlay(@NonNull final Overlay overlay) {
        Platform.runLater(() -> {
            overlays.remove(overlay);
            getChildren().remove(overlay.getNode());
        });
        return this;
    }

    public OverlayTarget hideAllOverlays() {
        overlays.forEach(this::hideOverlay);
        overlays.clear();
        return this;
    }

    public int overlayCount() {
        return overlays.size();
    }

    public boolean canShowOverlay() {
        return overlays.size() < maxCurrentOverlays;
    }
}
