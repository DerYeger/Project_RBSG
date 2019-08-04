package de.uniks.se19.team_g.project_rbsg.alert;

import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.ViewComponent;
import io.rincl.Rincled;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * @author Jan Müller
 */
@Component
public class AlertBuilder implements ApplicationContextAware, Rincled {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @NonNull
    private final SceneManager sceneManager;

    public enum Text {
        EXIT("exit"),
        CREATE_GAME_ERROR("createGameError"),
        CONNECTION_CLOSED("connectionClosed"),
        GAME_OVER("gameOver"),
        GAME_WON("gameWon"),
        GAME_SOMEBODY_ELSE_WON("elseWon"),
        GAME_LOST("gameLost"),
        INVALID_INPUT("invalidInput"),
        LOGOUT("logout"),
        NO_CONNECTION("noConnection"),
        PERMISSION_ERROR("permissionError"),
        END_PHASE("endPhase"),
        UNKNOWN_ERROR("unknownError");

        @NonNull
        private final String text;

        Text(@NonNull final String text) {
            this.text = text;
        }
    }

    public enum Type {
        CONFIRMATION("confirmationAlert"),
        INFO("infoAlert");

        @NonNull
        private final String bean;

        Type(@NonNull final String bean) {
            this.bean = bean;
        }
    }

    public AlertBuilder(@NonNull final SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    private ApplicationContext context;

    public void confirmation(@NonNull final Text text,
                             @Nullable final Runnable onConfirm,
                             @Nullable final Runnable onCancel) {
        confirmation(text, onConfirm, onCancel, null);
    }

    public void confirmation(@NonNull final Text text,
                             @Nullable final Runnable onConfirm,
                             @Nullable final Runnable onCancel,
                             @Nullable final String var) {
        try {
            ((ConfirmationAlertController) build(text, Type.CONFIRMATION, var))
                    .andThen(onConfirm)
                    .orElse(onCancel)
                    .show();
        } catch (final AlertCreationException e) {
            logger.debug("Unable to create alert: " + e.getMessage());
        }
    }

    public void priorityConfirmation(@NonNull final Text text,
                                     @Nullable final Runnable onConfirm,
                                     @Nullable final Runnable onCancel) {
        priorityConfirmation(text, onConfirm, onCancel, null);
    }

    public void priorityConfirmation(@NonNull final Text text,
                                     @Nullable final Runnable onConfirm,
                                     @Nullable final Runnable onCancel,
                                     @Nullable final String var) {
        Platform.runLater(() -> {
            clearActiveAlerts();
            confirmation(text, onConfirm, onCancel, var);
        });
    }

    public void information(@NonNull final Text text) {
        information(text, null, null);
    }

    public void information(@NonNull final Text text,
                            @Nullable final Runnable runnable) {
        information(text, runnable, null);
    }

    public void information(@NonNull final Text text,
                            @Nullable final Runnable runnable,
                            @Nullable final String var) {
        try {
            ((InfoAlertController) build(text, Type.INFO, var))
                    .andThen(runnable)
                    .show();
        } catch (final AlertCreationException e) {
            logger.debug("Unable to create alert:" + e.getMessage());
        }
    }

    public void priorityInformation(@NonNull final Text text,
                                    @Nullable final Runnable runnable) {
        priorityInformation(text, runnable, null);
    }

    public void priorityInformation(@NonNull final Text text,
                                    @Nullable final Runnable runnable,
                                    @Nullable final String var) {
        Platform.runLater(() -> {
            clearActiveAlerts();
            information(text, runnable, var);
        });
    }

    public AlertController build(@NonNull final Text text,
                                 @NonNull final Type type,
                                 @Nullable final String var) throws AlertCreationException {
        final StackPane target = sceneManager.getAlertTarget();

        if (target == null) {
            throw new AlertCreationException("No target");
        }

        if (target.getChildren().size() > 1) {
            throw new AlertCreationException("An alert is already active");
        }

        final StringBuilder message = new StringBuilder();
        if (var != null) {
            message.append(var).append(' ');
        }
        message.append(getResources().getString(text.text));

        @SuppressWarnings("unchecked")
        final ViewComponent<AlertController> components = (ViewComponent<AlertController>) context.getBean(type.bean);
        final AlertController controller = components.getController();
        controller.initialize(
                message.toString(),
                components.getRoot(),
                target);
        return controller;
    }

    private void clearActiveAlerts() {
        final ObservableList<Node> children = sceneManager.getAlertTarget().getChildren();
        for (int i = 1; i < children.size(); i++) {
            children.remove(i--);
        }
    }

    @Override
    public void setApplicationContext(@NonNull final ApplicationContext context) throws BeansException {
        this.context = context;
    }
}