package de.uniks.se19.team_g.project_rbsg.alert;

import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.ViewComponent;
import io.rincl.Rincled;
import javafx.scene.layout.StackPane;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * @author Jan Müller
 */
@Component
public class AlertBuilder implements ApplicationContextAware, Rincled {

    @NonNull
    private final SceneManager sceneManager;

    public enum Type {
        EXIT("exit", "confirmationAlert"),
        LOGOUT("logout", "confirmationAlert");

        @NonNull
        private final String text;
        @NonNull
        private final String bean;

        Type(@NonNull final String text, @NonNull final String bean) {
            this.text = text;
            this.bean = bean;
        }
    }

    public AlertBuilder(@NonNull final SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    private ApplicationContext context;

    public ConfirmationAlertController confirm(@NonNull final Type type) throws AlertCreationException {
        return (ConfirmationAlertController) build(type);
    }

    public AlertController build(@NonNull final Type type) throws AlertCreationException {
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
                getResources().getString(type.text),
                components.getRoot(),
                target);
        return controller;
    }

    @Override
    public void setApplicationContext(@NonNull final ApplicationContext context) throws BeansException {
        this.context = context;
    }
}
