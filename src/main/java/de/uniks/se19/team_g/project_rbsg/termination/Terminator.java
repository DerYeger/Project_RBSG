package de.uniks.se19.team_g.project_rbsg.termination;

import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.LogoutManager;
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
    @NonNull
    private final LogoutManager logoutManager;

    private HashSet<Terminable> registeredTerminables = new HashSet<>();

    @Autowired
    public Terminator(@NonNull final SceneManager sceneManager, @NonNull final LogoutManager logoutManager) {
        this.sceneManager = sceneManager;
        this.logoutManager = logoutManager;
    }

    public Terminator register(@NonNull final Terminable terminable) {
        registeredTerminables.add(terminable);
        logger.debug("Terminable " + terminable + " registered");
        return this;
    }

    public Terminator unregister(@NonNull final Terminable terminable) {
        registeredTerminables.remove(terminable);
        logger.debug("Terminable " + terminable + " unregistered");
        return this;
    }

    public Terminator terminateRootController() {
        sceneManager.terminateRootController();
        return this;
    }

    public Terminator terminateRegistered() {
        registeredTerminables.forEach(Terminable::terminate);
        return this;
    }

    public Terminator logoutUser() {
        logoutManager.logout();
        return this;
    }
}
