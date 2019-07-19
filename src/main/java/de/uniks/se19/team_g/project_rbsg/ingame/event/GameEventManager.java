package de.uniks.se19.team_g.project_rbsg.ingame.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.uniks.se19.team_g.project_rbsg.chat.ChatClient;
import de.uniks.se19.team_g.project_rbsg.chat.ChatController;
import de.uniks.se19.team_g.project_rbsg.server.websocket.WebSocketClient;
import de.uniks.se19.team_g.project_rbsg.server.websocket.WebSocketCloseHandler;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.websocket.CloseReason;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static de.uniks.se19.team_g.project_rbsg.ingame.event.CommandBuilder.endPhaseCommand;
import static de.uniks.se19.team_g.project_rbsg.ingame.event.CommandBuilder.leaveGameCommand;

/**
 * @author Jan Müller
 */
@Component
@Scope("prototype")
public class GameEventManager implements ChatClient, WebSocketCloseHandler {

    public static final String GAME_INIT_FINISHED = "gameInitFinished";
    public static final String GAME_STARTS = "gameStarts";

    private static final String ENDPOINT = "/game";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ArrayList<GameEventHandler> gameEventHandlers;

    private WebSocketClient webSocketClient;

    private ChatController chatController;

    private Runnable onConnectionClosed;

    private final CountDownLatch terminateLatch = new CountDownLatch(1);

    public GameEventManager(@NonNull final WebSocketClient webSocketClient) {
        this.webSocketClient = webSocketClient;

        gameEventHandlers = new ArrayList<>();
        gameEventHandlers.add(new DefaultGameEventHandler());

        webSocketClient.setCloseHandler(this);
    }

    public static boolean isActionType(ObjectNode message, String action) {
        return message.get("action").asText().equals(action);
    }

    @Override
    public void startChatClient(@NonNull final ChatController chatController) {
        this.chatController = chatController;
    }

    public GameEventManager addHandler(@NonNull final GameEventHandler gameEventHandler) {
        gameEventHandlers.add(gameEventHandler);
        return this;
    }

    public ArrayList<GameEventHandler> getHandlers() {
        return gameEventHandlers;
    }

    public void startSocket(@Nonnull final String gameID, @Nullable final String armyID) throws Exception {
        final URIBuilder uriBuilder = new URIBuilder("/game");
        uriBuilder.addParameter("gameId", gameID);
        if (armyID != null) {
            uriBuilder.addParameter("armyId", armyID);
        }
        webSocketClient.start(uriBuilder.build().toString(), this);
    }

    private String getGameIDParam(@NonNull final String gameID) {
        return "gameId=" + gameID;
    }

    private String getArmyIDParam(@NonNull final String armyID) {
        return "armyId=" + armyID;
    }

    @Override
    public void sendMessage(@NonNull final String channel, @Nullable final String to, @NonNull final String message) {
        webSocketClient.sendMessage(asJson(channel, to, message));
    }

    @NonNull
    private ObjectNode asJson(@NonNull final String channel, @Nullable final String  to, @NonNull final String content) {
        final ObjectNode node = new ObjectMapper().createObjectNode();

        node.put("messageType", "chat");

        if (channel.equals(CLIENT_PUBLIC_CHANNEL)) { //public message
            node.put("channel", SERVER_PUBLIC_CHANNEL);
        } else { //private message
            node.put("channel", SERVER_PRIVATE_CHANNEL);
            node.put("to", to);
        }

        node.put("message", content);

        return node;
    }

    @Override
    public void handle(@NonNull final String message) {
        final ObjectNode json;
        try {
            json = new ObjectMapper().readValue(message, ObjectNode.class);
            if (isChatMessage(json)) {
                chatController.receiveMessage(json.get("data"));
            } else if (isChatErrorMessage(json)) {
                chatController.receiveMessage(json);
            } else {
                gameEventHandlers.forEach(handler -> {
                    if (handler.accepts(json)) handler.handle(json);
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.debug("Error parsing message");
        }
    }

    public void sendMessage(Object message) {
        webSocketClient.sendMessage(message);
    }

    private boolean isChatMessage(@NonNull final ObjectNode message) {
        return message.has("action")
                && message.get("action").asText().equals("gameChat")
                && message.has("data");
    }

    private boolean isChatErrorMessage(@NonNull final ObjectNode message) {
        return message.has("msg");
    }

    private void sendLeaveCommand() {
        webSocketClient.sendMessage(leaveGameCommand());
    }

    public void sendEndPhaseCommand() {
        webSocketClient.sendMessage(endPhaseCommand());
    }

    public void setOnConnectionClosed(@Nonnull Runnable onConnectionClosed) {
        this.onConnectionClosed = onConnectionClosed;
    }

    @Override
    public void onSocketClosed(@NonNull CloseReason reason) {
        if (reason.getReasonPhrase().equals("Left game")) {
            terminateLatch.countDown();
        } else if (!reason.getReasonPhrase().equals("Tschau")) {
            onConnectionClosed.run();
        }
    }

    @Override
    public void terminate() {
        if (!webSocketClient.isClosed()) {
            try {
                sendLeaveCommand();
                if (!terminateLatch.await(5, TimeUnit.SECONDS)) {
                    webSocketClient.stop();
                    logger.debug("Terminated manually. Server did not respond in time");
                }
            } catch (final InterruptedException e) {
                logger.error(e.getMessage());
            }
        }
        logger.debug("Terminated " + this);
    }
}
