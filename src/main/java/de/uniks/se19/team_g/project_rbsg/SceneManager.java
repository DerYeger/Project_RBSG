package de.uniks.se19.team_g.project_rbsg;

import de.uniks.se19.team_g.project_rbsg.termination.Terminable;
import io.rincl.*;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @author Jan Müller
 */
@Component
public class SceneManager implements ApplicationContextAware, Terminable, Rincled {

    public enum SceneIdentifier {
        LOGIN("loginScene"),
        LOBBY("lobbyScene"),
        ARMY_BUILDER("armyScene"),
        INGAME("ingameScene"),
        BATTLEFIELD("battleFieldScene"),
        ;

        public final String builder;

        SceneIdentifier(@NonNull final String builder) {
            this.builder = builder;
        }
    }

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ApplicationContext context;

    private Stage stage;

    private HashMap<SceneIdentifier, Scene> cachedScenes = new HashMap<>();
    private HashMap<SceneIdentifier, RootController> rootControllers = new HashMap<>();

    private ExceptionHandler exceptionHandler;

    public SceneManager init(@NonNull final Stage stage) {
        this.stage = stage;
        stage.setResizable(false);
        stage.setTitle(String.format("%s - %s", getResources().getString("mainTitle"), getResources().getString("subTitle")));
        stage.getIcons().add(new Image(SceneManager.class.getResourceAsStream("/assets/icons/icon.png")));
        return this;
    }

    public SceneManager withExceptionHandler(@Nullable final ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
        return this;
    }

    public void setScene(@NonNull final SceneIdentifier sceneIdentifier, @NonNull final boolean useCaching, @Nullable final SceneIdentifier cacheIdentifier) {
        if (stage == null) {
            logger.error("Stage not initialised");
            return;
        }

        handleCaching(useCaching, cacheIdentifier);

        if (!useCaching) clearCache();

        logger.debug("Setting scene " + sceneIdentifier.name() + " with" + (useCaching ? " " : "out ") + "caching");

        if (cachedScenes.containsKey(sceneIdentifier)) {
            setSceneFromCache(sceneIdentifier);
            return;
        }

        try {
            @SuppressWarnings("unchecked")
            final ViewComponent<RootController> viewComponent = (ViewComponent<RootController>) context.getBean(sceneIdentifier.builder);
            showSceneFromViewComponent(viewComponent, sceneIdentifier);
        } catch (final Exception e) {
            logger.error(e.getMessage());
            if (exceptionHandler != null) exceptionHandler.handleException(this);
        }
    }

    private void handleCaching(@NonNull final boolean useCaching, @Nullable final SceneIdentifier cacheIdentifier) {
        if (useCaching && cacheIdentifier != null) {
            cachedScenes.put(cacheIdentifier, stage.getScene());
            logger.debug("Cached scene " + stage.getScene() + " with identifier " + cacheIdentifier.name());
        }
    }

    private void doSetScene(@NonNull final Scene scene) {
        Platform.runLater(() -> stage.setScene(scene));
    }

    private void setSceneFromCache(@NonNull final SceneIdentifier identifier) {
        if (cachedScenes.containsKey(identifier)) {
            doSetScene(cachedScenes.get(identifier));
        } else {
            logger.error("No cached Scene with identifier " + identifier.name());
        }
    }

    private void showSceneFromViewComponent(@NonNull final ViewComponent<RootController> component, @Nullable final SceneIdentifier identifier) {
        doSetScene(sceneFromParent(component.getRoot()));
        withRootController(component.getController(), identifier);
    }

    private Scene sceneFromParent(@NonNull final Parent parent)
    {
        return new Scene(parent);
    }

    public StackPane getAlertTarget() {
        if (stage == null) {
            logger.error("Stage not initialised");
            return null;
        }

        try {
            return (StackPane) stage.getScene().getRoot();
        } catch (final ClassCastException e) {
            e.printStackTrace();
            logger.error("Root of Scene " + stage.getScene() + " is not a StackPane");
        }
        return null;
    }

    public void withRootController(@NonNull final RootController rootController, @Nullable final SceneIdentifier identifier) {
        if (identifier != null) rootControllers.put(identifier, rootController);
    }

    private void clearCache() {
        terminateRootControllers();
        cachedScenes.clear();
        logger.debug("Cache cleared");
    }

    private void terminateRootControllers() {
        rootControllers.forEach((scene, controller) -> {
            if (controller instanceof Terminable) {
                ((Terminable) controller).terminate();
            }
        });
        rootControllers.clear();
        logger.debug("RootControllers terminated");
    }

    @Override
    public void terminate() {
        terminateRootControllers();
    }

    @Override
    public void setApplicationContext(@NonNull final ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
