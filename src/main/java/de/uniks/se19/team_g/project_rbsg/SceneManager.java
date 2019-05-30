package de.uniks.se19.team_g.project_rbsg;

import de.uniks.se19.team_g.project_rbsg.lobby.core.LobbySceneBuilder;
import de.uniks.se19.team_g.project_rbsg.login.LoginSceneBuilder;
import de.uniks.se19.team_g.project_rbsg.termination.RootController;
import de.uniks.se19.team_g.project_rbsg.termination.Terminable;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Jan Müller
 */
@Component
public class SceneManager implements ApplicationContextAware {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ApplicationContext context;

    private Stage stage;

    private RootController rootController;

    public SceneManager init(@NonNull final Stage stage) {
        this.stage = stage;
        return this;
    }

    public void setLoginScene() {
        if (stage == null) {
            logger.debug("Stage not initialised");
            return;
        }
        terminateRootController();
        try {
            final Scene loginScene = context.getBean(LoginSceneBuilder.class).getLoginScene();
            stage.setScene(loginScene);
        } catch (IOException e) {
            logger.error("Unable to set login scene");
            e.printStackTrace();
        }
    }

    public void setLobbyScene() {
        if (stage == null) {
            logger.debug("Stage not initialised");
            return;
        }
        terminateRootController();
        try {
            final Scene lobbyScene = context.getBean(LobbySceneBuilder.class).getLobbyScene();
            stage.setScene(lobbyScene);
        } catch (Exception e) {
            logger.error("Unable to set login scene");
            e.printStackTrace();
        }
    }

    public void terminateRootController() {
        if (rootController != null && rootController instanceof Terminable) {
            ((Terminable) rootController).terminate();
            rootController = null;
        }
    }

    public void setRootController(@NonNull final RootController rootController) {
        this.rootController = rootController;
    }

    @Override
    public void setApplicationContext(@NonNull final ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
