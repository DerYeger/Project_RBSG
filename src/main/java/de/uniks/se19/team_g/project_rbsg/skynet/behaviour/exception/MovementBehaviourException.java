package de.uniks.se19.team_g.project_rbsg.skynet.behaviour.exception;

import de.uniks.se19.team_g.project_rbsg.skynet.behaviour.exception.BehaviourException;
import org.springframework.lang.NonNull;

public class MovementBehaviourException extends BehaviourException {

    public MovementBehaviourException(@NonNull final String message) {
        super(message);
    }
}
