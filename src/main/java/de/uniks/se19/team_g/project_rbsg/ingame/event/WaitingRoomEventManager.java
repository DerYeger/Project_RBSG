package de.uniks.se19.team_g.project_rbsg.ingame.event;

import de.uniks.se19.team_g.project_rbsg.server.websocket.IWebSocketCallback;
import de.uniks.se19.team_g.project_rbsg.server.websocket.WebSocketClient;
import de.uniks.se19.team_g.project_rbsg.termination.Terminable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * @author Jan Müller
 */
@Component
@Scope("prototype")
public class WaitingRoomEventManager implements IWebSocketCallback, Terminable {

    private static final String ENDPOINT = "/game?gameId=";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ArrayList<WaitingRoomEventHandler> waitingRoomEventHandlers;

    private WebSocketClient webSocketClient;

    public WaitingRoomEventManager(@NonNull final WebSocketClient webSocketClient) {
        this.webSocketClient = webSocketClient;

        waitingRoomEventHandlers = new ArrayList<>();
        waitingRoomEventHandlers.add(new DefaultWaitingRoomEventHandler());
    }

    public WaitingRoomEventManager addHandler(@NonNull final WaitingRoomEventHandler waitingRoomEventHandler) {
        waitingRoomEventHandlers.add(waitingRoomEventHandler);
        return this;
    }

    public ArrayList<WaitingRoomEventHandler> getHandlers() {
        return waitingRoomEventHandlers;
    }

    public void startSocket(@NonNull final String gameID) {
        webSocketClient.start(ENDPOINT + gameID, this);
    }

    @Override
    public void handle(@NonNull final String message) {
        waitingRoomEventHandlers.forEach(handler -> handler.handle(message));
    }

    @Override
    public void terminate() {
        webSocketClient.stop();
        logger.debug("Terminated " + this);
    }
}
