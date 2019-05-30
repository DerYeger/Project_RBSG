package de.uniks.se19.team_g.project_rbsg.termination;

import de.uniks.se19.team_g.project_rbsg.SceneManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.HashSet;

/**
 * @author Jan Müller
 */
@Component
public class Terminator {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @NonNull
    private final SceneManager sceneManager;

    private HashSet<Terminable> registeredTerminables = new HashSet<>();

    @Autowired
    public Terminator(@NonNull final SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        logger.debug("Initialised with SceneManager " + sceneManager);
    }

    public void register(@NonNull final Terminable terminable) {
        registeredTerminables.add(terminable);
        logger.debug("Terminable " + terminable + " registered");
    }

    public void unregister(@NonNull final Terminable terminable) {
        registeredTerminables.remove(terminable);
        logger.debug("Terminable " + terminable + " unregistered");
    }

    public void terminateAll() {
        registeredTerminables.forEach(Terminable::terminate);
        logger.debug("All registered terminables terminated");
    }

    public void terminateRootController() {
        sceneManager.terminateRootController();
    }
}
