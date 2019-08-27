package de.uniks.se19.team_g.project_rbsg.scene;

import org.springframework.lang.NonNull;

public class SceneConfiguration {

    public static SceneConfiguration of(@NonNull final SceneManager.SceneIdentifier sceneIdentifier) {
        return new SceneConfiguration(sceneIdentifier);
    }

    private final SceneManager.SceneIdentifier sceneIdentifier;
    private SceneManager.SceneIdentifier cacheIdentifier = null;
    private ExceptionHandler exceptionHandler = null;

    private SceneConfiguration(@NonNull final SceneManager.SceneIdentifier sceneIdentifier) {
        this.sceneIdentifier = sceneIdentifier;
    }

    public SceneConfiguration andCache(@NonNull final SceneManager.SceneIdentifier cacheIdentifier) {
        this.cacheIdentifier = cacheIdentifier;
        return this;
    }

    public SceneConfiguration withExceptionHandler(@NonNull final ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
        return this;
    }

    public SceneManager.SceneIdentifier getSceneIdentifier() {
        return sceneIdentifier;
    }

    public SceneManager.SceneIdentifier getCacheIdentifier() {
        return cacheIdentifier;
    }

    public ExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof SceneConfiguration)) return false;

        final SceneConfiguration otherSceneConfiguration = (SceneConfiguration) other;

        return sceneIdentifier == otherSceneConfiguration.getSceneIdentifier()
                && cacheIdentifier == otherSceneConfiguration.getCacheIdentifier();
    }
}
