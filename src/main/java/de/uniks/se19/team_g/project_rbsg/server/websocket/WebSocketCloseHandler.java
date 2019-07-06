package de.uniks.se19.team_g.project_rbsg.server.websocket;

import org.springframework.lang.NonNull;

import javax.websocket.CloseReason;

/**
 * @author Jan Müller
 */
public interface WebSocketCloseHandler {

    void onSocketClosed(@NonNull CloseReason reason);
}
