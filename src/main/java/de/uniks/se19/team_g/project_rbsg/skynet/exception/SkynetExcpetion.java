package de.uniks.se19.team_g.project_rbsg.skynet.exception;

import org.springframework.lang.NonNull;

public class SkynetExcpetion extends Exception {

    public SkynetExcpetion(@NonNull final String message) {
        super(message);
    }
}
