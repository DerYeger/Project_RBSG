package de.uniks.se19.team_g.project_rbsg.termination;

import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.HashSet;

/**
 * @author Jan Müller
 */
public class Terminator {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ObservableSet<Terminable> registeredTerminables = FXCollections.observableSet(new HashSet<>());

    public ObservableSet<Terminable> getRegisteredTerminables() {
        return FXCollections.unmodifiableObservableSet(registeredTerminables);
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

    public Terminator terminate() {
        registeredTerminables.forEach(Terminable::terminate);
        registeredTerminables.clear();
        return this;
    }
}
