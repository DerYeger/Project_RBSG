package de.uniks.se19.team_g.project_rbsg.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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

@Component
@Scope("prototype")
public class LobbyChatClient implements ChatClient {

    public static final String ENDPOINT = "/chat?user=";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @NonNull
    private final WebSocketClient webSocketClient;

    @NonNull
    private final UserProvider userProvider;
    private ChatController chatController;

    public LobbyChatClient(@NonNull final WebSocketClient webSocketClient, @NonNull final UserProvider userProvider) {
        this.webSocketClient = webSocketClient;
        this.userProvider = userProvider;
    }

    @Override
    public void start(@NonNull final ChatController chatController) {
        this.chatController = chatController;
        try {
            webSocketClient.start(ENDPOINT + URLEncoder.encode(userProvider.get().getName(), StandardCharsets.UTF_8.name()), this);
        } catch (final UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessage(@NonNull final String  channel, @Nullable final String  to, @NonNull final String  message) {
        final ObjectNode json = asJson(channel, to, message);
        webSocketClient.sendMessage(json);
    }

    @NonNull
    private ObjectNode asJson(@NonNull final String channel, @Nullable final String  to, @NonNull final String content) {
        final ObjectMapper objectMapper = new ObjectMapper();
        final ObjectNode node = objectMapper.createObjectNode();

        if (channel.equals(CLIENT_PUBLIC_CHANNEL)) { //public message
            node.put("channel", SERVER_PUBLIC_CHANNEL);
        } else { //private message
            node.put("channel", SERVER_PRIVATE_CHANNEL);
            node.put("to", to);
        }

        node.put("message", content);

        return node;
    }

    //TEMP BEGIN
    @Override
    public void handle(@NonNull final String message) {
        try {
            final ObjectNode json = new ObjectMapper().readValue(message, ObjectNode.class);

            if (!json.has("channel") || !json.has("from") || !json.has("message")) {
                handleErrorMessage(json);
            } else {
                final String channel = json.get("channel").asText();
                final String from = json.get("from").asText();
                final String content = json.get("message").asText();

                final String internalChannel = channel.equals(SERVER_PUBLIC_CHANNEL) ? ChatController.GENERAL_CHANNEL_NAME : '@' + from;

                chatController.receiveMessage(internalChannel, from, content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleErrorMessage(@NonNull final ObjectNode json) {
        if (json.has("msg")) {
            chatController.receiveErrorMessage(json.get("msg").asText());
        } else {
            System.out.println("Server response has unknown format: "  + json.toString());
        }
    }
    //TEMP END

    @Override
    public void terminate() {
        webSocketClient.stop();
        logger.debug("Terminated " + this);
    }
}
