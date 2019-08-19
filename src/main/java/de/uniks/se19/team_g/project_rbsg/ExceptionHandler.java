package de.uniks.se19.team_g.project_rbsg;

import de.uniks.se19.team_g.project_rbsg.overlay.alert.AlertBuilder;
import de.uniks.se19.team_g.project_rbsg.server.websocket.WebSocketConfigurator;
import de.uniks.se19.team_g.project_rbsg.termination.Terminator;
import javafx.application.Platform;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ExceptionHandler {

    @NonNull
    private final Terminator terminator;
    private final AlertBuilder alertBuilder;

    public ExceptionHandler(@NonNull final Terminator terminator,
                            @NonNull final AlertBuilder alertBuilder) {
        this.terminator = terminator;
        this.alertBuilder = alertBuilder;
    }

    public void handleException(@NonNull final SceneManager sceneManager) {
        terminator.terminate();
        WebSocketConfigurator.userKey = "";
        sceneManager.setScene(SceneManager.SceneIdentifier.LOGIN, false, null);
        Platform.runLater(() -> alertBuilder.information(AlertBuilder.Text.PERMISSION_ERROR));
    }
}
