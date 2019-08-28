package de.uniks.se19.team_g.project_rbsg.scene;

import de.uniks.se19.team_g.project_rbsg.ProjectRbsgFXApplication;
import de.uniks.se19.team_g.project_rbsg.overlay.OverlayTarget;
import de.uniks.se19.team_g.project_rbsg.overlay.OverlayTargetProvider;
import de.uniks.se19.team_g.project_rbsg.termination.Terminable;
import io.rincl.Rincled;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Primary;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @author Jan Müller
 */
@Primary
@Component
public class SceneManager implements ApplicationContextAware, Rincled, OverlayTargetProvider, Terminable {

    public enum SceneIdentifier {
        LOGIN("loginScene"),
        LOBBY("lobbyScene"),
        ARMY_BUILDER("armyScene"),
        INGAME("ingameScene");

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

    private DefaultExceptionHandler exceptionHandler;

    public SceneManager init(@NonNull final Stage stage) {
        this.stage = stage;
        initStage();
        return this;
    }

    //package private for tests
    void initStage() {
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setMinHeight(780);
        stage.setMinWidth(640);
        setResizeableFalse();
        stage.setTitle(String.format("%s - %s", getResources().getString("mainTitle"), getResources().getString("subTitle")));
        stage.getIcons().add(new Image(SceneManager.class.getResourceAsStream("/assets/icons/icon.png")));
    }

    public SceneManager withExceptionHandler(@Nullable final DefaultExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
        return this;
    }

    public void setScene(@NonNull final SceneConfiguration sceneConfiguration) {
        try {
            unhandledSetScene(sceneConfiguration);
        } catch (final RuntimeException e) {
            if (sceneConfiguration.getExceptionHandler() != null) {
                sceneConfiguration.getExceptionHandler().handle(e);
            } else if (exceptionHandler != null) {
                exceptionHandler.handle(e);
            } else {
                logger.info("No exception handler available");
            }
        }
    }

    //package private for tests
    void unhandledSetScene(@NonNull final SceneConfiguration sceneConfiguration) {
        if (stage == null) {
            logger.error("Stage not initialised");
            return;
        } else {
            stage.setHeight(ProjectRbsgFXApplication.HEIGHT);
            stage.setWidth(ProjectRbsgFXApplication.WIDTH);
            setResizeableFalse();
        }

        final boolean withCaching = handleCaching(sceneConfiguration);

        if (withCaching) clearCache();

        final SceneIdentifier sceneIdentifier = sceneConfiguration.getSceneIdentifier();

        logger.debug("Setting scene " + sceneIdentifier.name() + " with" + (withCaching ? " " : "out ") + "caching");

        if (cachedScenes.containsKey(sceneIdentifier)) {
            setSceneFromCache(sceneIdentifier);
            return;
        }

        @SuppressWarnings("unchecked")
        final ViewComponent<RootController> viewComponent = (ViewComponent<RootController>) context.getBean(sceneIdentifier.builder);
        showSceneFromViewComponent(viewComponent, sceneIdentifier);
    }

    private boolean handleCaching(@NonNull final SceneConfiguration sceneConfiguration) {
        final SceneIdentifier cacheIdentifier = sceneConfiguration.getCacheIdentifier();
        if (cacheIdentifier != null) {
            cachedScenes.put(cacheIdentifier, stage.getScene());
            logger.debug("Cached scene " + stage.getScene() + " with identifier " + cacheIdentifier.name());
            return true;
        }
        return false;
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
        return new Scene(OverlayTarget.of(parent));
    }

    @Override
    public OverlayTarget getOverlayTarget() {
        final Parent root = stage.getScene().getRoot();
        if (!(root instanceof OverlayTarget)) return null;
        return (OverlayTarget) root;
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

    public void setResizeableTrue(){
        this.stage.setResizable(true);
    }

    public void setResizeableFalse(){
        this.stage.setResizable(false);
    }

    public void setFullscreen() {
        this.stage.setFullScreen(true);
    }

    public void unsetFullscreen() {
        this.stage.setFullScreen(false);
    }

    public boolean isFullscreen() {
        return this.stage.isFullScreen();
    }

    public ReadOnlyDoubleProperty getStageHeightProperty() {
        return stage.heightProperty();
    }

    public ReadOnlyDoubleProperty getStageWidhtProperty() {
        return stage.widthProperty();
    }

    public boolean isStageInit() {
        return this.stage != null;
    }
}
