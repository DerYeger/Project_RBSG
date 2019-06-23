package de.uniks.se19.team_g.project_rbsg;

import de.uniks.se19.team_g.project_rbsg.ingame.IngameSceneBuilder;
import de.uniks.se19.team_g.project_rbsg.termination.RootController;
import de.uniks.se19.team_g.project_rbsg.waiting_room.WaitingRoomSceneBuilder;
import de.uniks.se19.team_g.project_rbsg.lobby.core.LobbySceneBuilder;
import de.uniks.se19.team_g.project_rbsg.login.StartSceneBuilder;
import de.uniks.se19.team_g.project_rbsg.termination.Terminable;
import io.rincl.*;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author Jan Müller
 */
@Component
public class SceneManager implements ApplicationContextAware, Terminable, Rincled {

    public enum SceneIdentifier {
        LOGIN,
        LOBBY,
        ARMY_BUILDER,
        WAITING_ROOM
    }

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ApplicationContext context;

    private Stage stage;

    private HashMap<SceneIdentifier, Scene> cachedScenes = new HashMap<>();
    private HashSet<RootController> rootControllers = new HashSet<>();

    public SceneManager init(@NonNull final Stage stage) {
        this.stage = stage;
        stage.setTitle(String.format("%s - %s", getResources().getString("mainTitle"), getResources().getString("subTitle")));
        stage.getIcons().add(new Image(SceneManager.class.getResourceAsStream("/assets/icons/icon.png")));
        return this;
    }

    public void withRootController(@NonNull final RootController rootController) {
        rootControllers.add(rootController);
    }

    public HashSet<RootController> getRootControllers() {
        return rootControllers;
    }

    private void setScene(@NonNull final Scene scene) {
        Platform.runLater(() -> stage.setScene(scene));
    }

    private void setSceneFromCache(@NonNull final SceneIdentifier identifier) {
        if (cachedScenes.containsKey(identifier)) {
            setScene(cachedScenes.get(identifier));
        } else {
            logger.error("No cached Scene with identifier " + identifier.name());
        }
    }

    public void setStartScene() {
        if (stage == null) {
            logger.error("Stage not initialised");
            return;
        }
        try {
            clear();
            final Scene startScene = context.getBean(StartSceneBuilder.class).getStartScene();
            setScene(startScene);
        } catch (IOException e) {
            logger.error("Unable to set start scene");
            e.printStackTrace();
        }
    }

    public void setLobbyScene(@NonNull final boolean useCache, @Nullable final SceneIdentifier cacheIdentifier) {
        if (stage == null) {
            logger.error("Stage not initialised");
            return;
        }
        try {
            handleCaching(useCache, cacheIdentifier);

            if (!useCache) clear();

            if (cachedScenes.containsKey(SceneIdentifier.LOBBY)) {
                setSceneFromCache(SceneIdentifier.LOBBY);
                return;
            }

            final Scene lobbyScene = context.getBean(LobbySceneBuilder.class).getLobbyScene();
            setScene(lobbyScene);
        } catch (Exception e) {
            System.out.println("Unable to set lobby scene");
            e.printStackTrace();
        }
    }

    public void setWaitingRoomScene() {
        if (stage == null) {
            logger.error("Stage not initialised");
            return;
        }
        try {
            clear();
            final Scene waitingRoomScene = context.getBean(WaitingRoomSceneBuilder.class).getWaitingRoomScene();
            setScene(waitingRoomScene);
        } catch (Exception e) {
            System.out.println("Unable to set waiting_room scene");
            e.printStackTrace();
        }
    }

    public void setIngameScene() {
        if (stage == null) {
            System.out.println("Not yet initialised");
            return;
        }
        try {
            final Scene ingameScene = context.getBean(IngameSceneBuilder.class).getIngameScene();
            stage.setResizable(false);
            setScene(ingameScene);
        } catch (Exception e) {
            System.out.println("Unable to set ingame scene");
            e.printStackTrace();
        }
    }

    public void setArmyBuilderScene(@NonNull final boolean useCache, @Nullable final SceneIdentifier cacheIdentifier) {
        handleCaching(useCache, cacheIdentifier);

        if (!useCache) clear();

        if (cachedScenes.containsKey(SceneIdentifier.ARMY_BUILDER)) {
            setSceneFromCache(SceneIdentifier.ARMY_BUILDER);
            return;
        }

        @SuppressWarnings("unchecked") ViewComponent<RootController> component
                = (ViewComponent<RootController>) context.getBean("armyBuilderScene");
        showSceneFromViewComponent(component);
    }

    private void handleCaching(@NonNull final boolean useCache, @Nullable final SceneIdentifier cacheIdentifier) {
        if (useCache && cacheIdentifier != null) {
            cachedScenes.put(cacheIdentifier, stage.getScene());
        }
    }

    private void showSceneFromViewComponent(@NonNull final ViewComponent<RootController> component) {
        setScene(sceneFromParent(component.getRoot()));
        withRootController(component.getController());
    }

    public Scene sceneFromParent(@NonNull final Parent parent)
    {
        return new Scene(parent);
    }

    @Override
    public void setApplicationContext(@NonNull final ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    private void clear() {
        terminateRootControllers();
        cachedScenes.clear();
    }

    private void terminateRootControllers() {
        for (final RootController controller : rootControllers) {
            if (controller instanceof Terminable) {
                ((Terminable) controller).terminate();
            }
        }
        rootControllers.clear();
    }

    @Override
    public void terminate() {
        terminateRootControllers();
    }
}
