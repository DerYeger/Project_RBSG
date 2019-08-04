package de.uniks.se19.team_g.project_rbsg.alert;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import org.springframework.lang.NonNull;

/**
 * @author Jan Müller
 */
public abstract class AlertController {

    protected String text;
    protected Node alert;
    protected StackPane target;

    public void initialize(@NonNull final String text, @NonNull final Node alert, @NonNull final StackPane target) {
        this.text = text;
        this.alert = alert;
        this.target = target;
        init();
    }

    protected abstract void init();

    public void show() {
        if (!target.getChildren().contains(alert)) {
            Platform.runLater(() -> {
                target.getChildren().add(alert);
                alert.setFocusTraversable(true);
                alert.requestFocus();
            });
        }
    }

    public void hide() {
        Platform.runLater(() -> target.getChildren().remove(alert));
    }
}
