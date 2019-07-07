package de.uniks.se19.team_g.project_rbsg.lobby.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.uniks.se19.team_g.project_rbsg.chat.ChatClient;
import de.uniks.se19.team_g.project_rbsg.chat.ChatController;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.server.websocket.WebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author Jan Müller
 */
@Component
@Scope("prototype")
public class LobbyChatClient implements ChatClient {

    private static final String ENDPOINT = "/chat?user=";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @NonNull
    private final WebSocketClient webSocketClient;

    @NonNull
    private final UserProvider userProvider;

    private ChatController chatController;

    private ObjectMapper objectMapper;

    public LobbyChatClient(@NonNull final WebSocketClient webSocketClient, @NonNull final UserProvider userProvider) {
        this.webSocketClient = webSocketClient;
        this.userProvider = userProvider;
        objectMapper = new ObjectMapper();
    }

    @Override
    public void startChatClient(@NonNull final ChatController chatController) throws Exception {
        this.chatController = chatController;
        webSocketClient.start(ENDPOINT + URLEncoder.encode(userProvider.get().getName(), StandardCharsets.UTF_8.name()), this);
    }

    @Override
    public void sendMessage(@NonNull final String  channel, @Nullable final String  to, @NonNull final String  message) {
        webSocketClient.sendMessage(asJson(channel, to, message));
    }

    @NonNull
    private ObjectNode asJson(@NonNull final String channel, @Nullable final String  to, @NonNull final String content) {
        final ObjectNode node = new ObjectMapper().createObjectNode();

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
        try {
            chatController.receiveMessage(objectMapper.readValue(message, ObjectNode.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void terminate() {
        webSocketClient.stop();
        logger.debug("Terminated " + this);
    }
}
