package de.uniks.se19.team_g.project_rbsg.server.websocket;

import org.springframework.lang.NonNull;

public class WebSocketException extends Exception {

    public WebSocketException(@NonNull final String message) {
        super(message);
    }
}
