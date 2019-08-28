package de.uniks.se19.team_g.project_rbsg.scene;

import de.uniks.se19.team_g.project_rbsg.overlay.alert.AlertBuilder;
import de.uniks.se19.team_g.project_rbsg.termination.Terminator;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static de.uniks.se19.team_g.project_rbsg.scene.SceneManager.SceneIdentifier.LOGIN;

@Component
public class DefaultExceptionHandler implements ExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Terminator terminator;
    private final AlertBuilder alertBuilder;
    private final SceneManager sceneManager;

    public DefaultExceptionHandler(@NonNull final Terminator terminator,
                                   @NonNull final AlertBuilder alertBuilder,
                                   @NonNull final SceneManager sceneManager) {
        this.terminator = terminator;
        this.alertBuilder = alertBuilder;
        this.sceneManager = sceneManager;
    }

    public void handle(@NonNull final Exception exception) {
        logger.debug("Handling " + exception);
        terminator.terminate();
        sceneManager.setScene(SceneConfiguration.of(LOGIN));
        Platform.runLater(() -> alertBuilder.priorityInformation(AlertBuilder.Text.OOPS));
    }
}
