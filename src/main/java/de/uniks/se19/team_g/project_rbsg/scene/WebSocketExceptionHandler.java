package de.uniks.se19.team_g.project_rbsg.scene;

import de.uniks.se19.team_g.project_rbsg.overlay.alert.AlertBuilder;
import de.uniks.se19.team_g.project_rbsg.server.websocket.WebSocketException;
import de.uniks.se19.team_g.project_rbsg.util.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

public class WebSocketExceptionHandler implements ExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final AlertBuilder alertBuilder;

    private Runnable onRetry;
    private Runnable onCancel;

    public WebSocketExceptionHandler(@NonNull final AlertBuilder alertBuilder) {
        this.alertBuilder = alertBuilder;
    }

    public WebSocketExceptionHandler onRetry(@NonNull final Runnable onRetry) {
        this.onRetry = onRetry;
        return this;
    }

    public WebSocketExceptionHandler onCancel(@NonNull final Runnable onCancel) {
        this.onCancel = onCancel;
        return this;
    }

    @Override
    public void handle(@NonNull final Exception exception) {
        logger.debug("Handling " + exception);
        final AlertBuilder.Text text =
                ExceptionUtils.rootCause(exception) instanceof WebSocketException
                        ? AlertBuilder.Text.SOCKET : AlertBuilder.Text.OOPS;
        alertBuilder.priorityConfirmation(
                text,
                onRetry,
                onCancel
        );
    }
}
