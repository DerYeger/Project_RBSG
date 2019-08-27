package de.uniks.se19.team_g.project_rbsg.scene;

import de.uniks.se19.team_g.project_rbsg.overlay.alert.AlertBuilder;
import de.uniks.se19.team_g.project_rbsg.server.websocket.WebSocketConfigurator;
import de.uniks.se19.team_g.project_rbsg.termination.Terminator;
import javafx.application.Platform;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static de.uniks.se19.team_g.project_rbsg.scene.SceneManager.SceneIdentifier.*;

@Component
public class DefaultExceptionHandler {

    private final Terminator terminator;
    private final AlertBuilder alertBuilder;

    public DefaultExceptionHandler(@NonNull final Terminator terminator,
                                   @NonNull final AlertBuilder alertBuilder) {
        this.terminator = terminator;
        this.alertBuilder = alertBuilder;
    }

    public void handleException(@NonNull final SceneManager sceneManager) {
        terminator.terminate();
        WebSocketConfigurator.userKey = "";
        sceneManager.setScene(SceneConfiguration.of(LOGIN));
        Platform.runLater(() -> alertBuilder.information(AlertBuilder.Text.OOPS));
    }
}
