package de.uniks.se19.team_g.project_rbsg.skynet.behaviour.exception;

import org.springframework.lang.NonNull;

public class FallbackBehaviourException extends BehaviourException {

    public FallbackBehaviourException(@NonNull final String message) {
        super(message);
    }
}
