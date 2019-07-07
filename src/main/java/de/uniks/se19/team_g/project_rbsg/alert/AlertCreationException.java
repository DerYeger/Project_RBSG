package de.uniks.se19.team_g.project_rbsg.alert;

import javax.validation.constraints.Null;

public class AlertCreationException extends Exception {
    public AlertCreationException(@Null final String message) {
        super(message);
    }
}
