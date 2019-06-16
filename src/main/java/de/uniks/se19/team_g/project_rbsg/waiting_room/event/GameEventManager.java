package de.uniks.se19.team_g.project_rbsg.waiting_room.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.uniks.se19.team_g.project_rbsg.chat.ChatClient;
import de.uniks.se19.team_g.project_rbsg.chat.ChatController;
import de.uniks.se19.team_g.project_rbsg.waiting_room.model.ModelManager;
import de.uniks.se19.team_g.project_rbsg.server.websocket.WebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;

import static de.uniks.se19.team_g.project_rbsg.ingame.event.CommandBuilder.leaveGameCommand;

/**
 * @author Jan Müller
 */
@Component
@Scope("prototype")
public class GameEventManager implements ChatClient {

    private static final String ENDPOINT = "/game?";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ArrayList<GameEventHandler> gameEventHandlers;

    private WebSocketClient webSocketClient;

    private ChatController chatController;

    public GameEventManager(@NonNull final WebSocketClient webSocketClient) {
        this.webSocketClient = webSocketClient;

        gameEventHandlers = new ArrayList<>();
        gameEventHandlers.add(new ModelManager());
        gameEventHandlers.add(new DefaultGameEventHandler());
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

    public void startSocket(@NonNull final String gameID, @NonNull final String armyID) {
        webSocketClient.start(ENDPOINT + getGameIDParam(gameID) + '&' + getArmyIDParam(armyID), this);
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
                //TODO: Check the server's reply format and parse message
                System.out.println(json);
            } else {
                gameEventHandlers.forEach(handler -> handler.handle(json));
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.debug("Error parsing message");
        }
    }

    private boolean isChatMessage(@NonNull final ObjectNode json) {
        return json.has("messageType")
                && json.get("messageType").asText().equals("chat");
    }

    @Override
    public void terminate() {
        sendLeaveCommand();
        webSocketClient.stop();
        logger.debug("Terminated " + this);
    }

    private void sendLeaveCommand() {
        webSocketClient.sendMessage(leaveGameCommand());
    }
}