package de.uniks.se19.team_g.project_rbsg.overlay.alert;

import javax.validation.constraints.Null;

public class AlertException extends Exception {
    public AlertException(@Null final String message) {
        super(message);
    }
}
