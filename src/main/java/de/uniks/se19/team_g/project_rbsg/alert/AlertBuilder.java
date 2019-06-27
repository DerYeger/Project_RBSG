package de.uniks.se19.team_g.project_rbsg.alert;

import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.ViewComponent;
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
public class AlertBuilder implements ApplicationContextAware {

    @NonNull
    private final SceneManager sceneManager;

    public enum Type {
        EXIT("exitAlert"),
        LOGOUT("logoutAlert");

        @NonNull
        private final String beanName;

        Type(@NonNull final String beanName) {
            this.beanName = beanName;
        }
    }

    public AlertBuilder(@NonNull final SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    private ApplicationContext context;

    public ConfirmationAlertController confirm(@NonNull final Type type) {
        return (ConfirmationAlertController) build(type);
    }

    public AlertController build(@NonNull final Type type) {
        final StackPane target = sceneManager.getAlertTarget();

        if (target == null) return null;

        if (target.getChildren().size() > 1) return null;

        @SuppressWarnings("unchecked")
        final ViewComponent<AlertController> components = (ViewComponent<AlertController>) context.getBean(type.beanName);
        final AlertController controller = components.getController();
        controller.initialize(components.getRoot(), target);
        return controller;
    }

    @Override
    public void setApplicationContext(@NonNull final ApplicationContext context) throws BeansException {
        this.context = context;
    }
}
