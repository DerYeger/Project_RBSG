package de.uniks.se19.team_g.project_rbsg.ingame.event;

import de.uniks.se19.team_g.project_rbsg.server.websocket.IWebSocketCallback;
import de.uniks.se19.team_g.project_rbsg.server.websocket.WebSocketClient;
import de.uniks.se19.team_g.project_rbsg.termination.Terminable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * @author Jan Müller
 */
@Component
public class GameEventHandlerManager implements IWebSocketCallback, Terminable {

    private static final String ENDPOINT = "/game?gameId=";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ArrayList<GameEventHandler> gameEventHandlers;

    private WebSocketClient webSocketClient;

    private int gameID;

    public GameEventHandlerManager(@NonNull final WebSocketClient webSocketClient) {
        this.webSocketClient = webSocketClient;

        gameEventHandlers = new ArrayList<>();
        gameEventHandlers.add(new DefaultGameEventHandler());
    }

    public GameEventHandlerManager addHandler(@NonNull final GameEventHandler gameEventHandler) {
        gameEventHandlers.add(gameEventHandler);
        return this;
    }

    public ArrayList<GameEventHandler> getHandlers() {
        return gameEventHandlers;
    }

    public void startSocket(final int gameID) {
        this.gameID = gameID;
        webSocketClient.start(ENDPOINT + gameID, this);
    }

    @Override
    public void handle(@NonNull final String message) {
        gameEventHandlers.forEach(handler -> handler.handle(message));
    }

    @Override
    public void terminate() {
        webSocketClient.stop();
    }
}
