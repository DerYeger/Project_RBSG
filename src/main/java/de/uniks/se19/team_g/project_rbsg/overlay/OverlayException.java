package de.uniks.se19.team_g.project_rbsg.overlay;

import javax.validation.constraints.Null;

public class OverlayException extends Exception {

    public OverlayException(@Null final String message) {
        super(message);
    }
}
