package de.uniks.se19.team_g.project_rbsg.ingame.event;

import de.uniks.se19.team_g.project_rbsg.server.websocket.IWebSocketCallback;
import de.uniks.se19.team_g.project_rbsg.server.websocket.WebSocketClient;
import de.uniks.se19.team_g.project_rbsg.server.websocket.WebSocketConfigurator;
import de.uniks.se19.team_g.project_rbsg.termination.Terminable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import java.util.ArrayList;

/**
 * @author Jan Müller
 */
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

    public GameEventHandlerManager addGameEventHandler(@NonNull final GameEventHandler gameEventHandler) {
        gameEventHandlers.add(gameEventHandler);
        return this;
    }

    public void startSocket(final int gameID) {
        if (WebSocketConfigurator.userKey.equals("") || webSocketClient == null) {
            return;
        }
        this.gameID = gameID;
        webSocketClient.start(ENDPOINT + gameID, this);
    }

    @Override
    public void handle(@NonNull final String message) {
        gameEventHandlers.forEach(gameEventHandler -> handle(message));
    }

    @Override
    public void terminate() {
        webSocketClient.stop();
    }
}
