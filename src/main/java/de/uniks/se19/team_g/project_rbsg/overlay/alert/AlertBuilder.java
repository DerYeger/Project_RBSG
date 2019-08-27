package de.uniks.se19.team_g.project_rbsg.overlay.alert;

import de.uniks.se19.team_g.project_rbsg.scene.ViewComponent;
import de.uniks.se19.team_g.project_rbsg.overlay.OverlayException;
import de.uniks.se19.team_g.project_rbsg.overlay.Overlay;
import de.uniks.se19.team_g.project_rbsg.overlay.OverlayTarget;
import de.uniks.se19.team_g.project_rbsg.overlay.OverlayTargetProvider;
import io.rincl.Rincled;
import javafx.application.Platform;
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

    private final OverlayTargetProvider overlayTargetProvider;

    public enum Text {
        CONNECTION_CLOSED("connectionClosed"),
        CREATE_GAME_ERROR("createGameError"),
        EGG_WON("eggWon"),
        EGG_LOST("eggLost"),
        END_PHASE("endPhase"),
        EXIT("exit"),
        GAME_LOST("gameLost"),
        GAME_SOMEBODY_ELSE_WON("elseWon"),
        GAME_WON("gameWon"),
        INVALID_INPUT("invalidInput"),
        LOGOUT("logout"),
        NO_CONNECTION("noConnection"),
        OOPS("oops"),
        SOCKET("socket"),
        SURRENDER("surrender"),
        UNSAVED_ARMY("unsaved_army");

        private final String text;

        Text(@NonNull final String text) {
            this.text = text;
        }
    }

    public enum Type {
        CONFIRMATION("confirmationAlertView"),
        INFO("infoAlertView");

        private final String bean;

        Type(@NonNull final String bean) {
            this.bean = bean;
        }
    }

    public AlertBuilder(@NonNull final OverlayTargetProvider overlayTargetProvider) {
        this.overlayTargetProvider = overlayTargetProvider;
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
            ((ConfirmationAlert) build(text, Type.CONFIRMATION, var))
                    .andThen(onConfirm)
                    .orElse(onCancel)
                    .show();
        } catch (final OverlayException e) {
            logger.info("Unable to create alert: " + e.getMessage());
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
            overlayTargetProvider.getOverlayTarget().hideAllOverlays();
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
            ((InfoAlert) build(text, Type.INFO, var))
                    .andThen(runnable)
                    .show();
        } catch (final OverlayException e) {
            logger.debug("Unable to create alert: " + e.getMessage());
        }
    }

    public void priorityInformation(@NonNull final Text text) {
        priorityInformation(text, null, null);
    }

    public void priorityInformation(@NonNull final Text text,
                                    @Nullable final Runnable runnable) {
        priorityInformation(text, runnable, null);
    }

    public void priorityInformation(@NonNull final Text text,
                                    @Nullable final Runnable runnable,
                                    @Nullable final String var) {
        Platform.runLater(() -> {
            overlayTargetProvider.getOverlayTarget().hideAllOverlays();
            information(text, runnable, var);
        });
    }

    public Overlay build(@NonNull final Text text,
                         @NonNull final Type type,
                         @Nullable final String var) throws OverlayException {
        final OverlayTarget target = overlayTargetProvider.getOverlayTarget();

        if (target == null) {
            throw new OverlayException("No target available");
        }

        if (!target.canShowOverlay()) {
            throw new OverlayException("An overlay is already active");
        }

        final StringBuilder message = new StringBuilder();
        if (var != null) {
            message
                    .append(var)
                    .append(' ');
        }
        message.append(getResources().getString(text.text));

        @SuppressWarnings("unchecked")
        final ViewComponent<Overlay> components = (ViewComponent<Overlay>) context.getBean(type.bean);
        final Overlay controller = components.getController();
        controller.initialize(
                message.toString(),
                components.getRoot(),
                target);
        return controller;
    }

    @Override
    public void setApplicationContext(@NonNull final ApplicationContext context) throws BeansException {
        this.context = context;
    }
}