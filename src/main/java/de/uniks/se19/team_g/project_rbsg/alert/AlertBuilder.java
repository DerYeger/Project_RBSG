package de.uniks.se19.team_g.project_rbsg.alert;

import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.ViewComponent;
import io.rincl.Rincled;
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
        INVALID_INPUT("invalidInput"),
        LOGOUT("logout"),
        NO_CONNECTION("noConnection"),
        UNKNOWN_ERROR("unknownError"),
        CONNECTION_CLOSED("connectionClosed");

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

    public void confirmation(@NonNull final Text text, @NonNull final Runnable onConfirm, @Nullable final Runnable onCancel) {
        try {
            ((ConfirmationAlertController) build(text, Type.CONFIRMATION))
                    .andThen(onConfirm)
                    .orElse(onCancel)
                    .show();
        } catch (final AlertCreationException e) {
            logger.debug("Unable to create alert: " + e.getMessage());
        }
    }

    public void information(@NonNull final Text text) {
        try {
            build(text, Type.INFO).show();
        } catch (final AlertCreationException e) {
            logger.debug("Unable to create alert:" + e.getMessage());
        }
    }

    public void error(@NonNull final Text text, @Nullable final Runnable runnable) {
        try {
            ((InfoAlertController) build(text, Type.INFO))
                    .andThen(runnable)
                    .show();
        } catch (final AlertCreationException e) {
            logger.debug("Unable to create alert:" + e.getMessage());
        }
    }

    public AlertController build(@NonNull final Text text, @NonNull final Type type) throws AlertCreationException {
        final StackPane target = sceneManager.getAlertTarget();

        if (target == null) {
            throw new AlertCreationException("No target");
        }

        if (target.getChildren().size() > 1) {
            throw new AlertCreationException("An alert is already active");
        }

        @SuppressWarnings("unchecked")
        final ViewComponent<AlertController> components = (ViewComponent<AlertController>) context.getBean(type.bean);
        final AlertController controller = components.getController();
        controller.initialize(
                getResources().getString(text.text),
                components.getRoot(),
                target);
        return controller;
    }

    @Override
    public void setApplicationContext(@NonNull final ApplicationContext context) throws BeansException {
        this.context = context;
    }
}
