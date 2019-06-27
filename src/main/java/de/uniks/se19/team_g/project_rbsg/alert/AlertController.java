package de.uniks.se19.team_g.project_rbsg.alert;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import org.springframework.lang.NonNull;

/**
 * @author Jan Müller
 */
public abstract class AlertController {

    protected StackPane target;

    protected Node alert;

    public void initialize(@NonNull final Node alert, @NonNull final StackPane target) {
        this.alert = alert;
        this.target = target;
    }

    public abstract void initialize();

    public void show() {
        if (!target.getChildren().contains(alert)) {
            target.getChildren().add(alert);
        }
    }

    public void hide() {
        target.getChildren().remove(alert);
    }
}
