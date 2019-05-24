package de.uniks.se19.team_g.project_rbsg.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.Contract.IWebSocketCallback;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Jan Müller
 */
@Component
public class ChatWebSocketCallback implements IWebSocketCallback {

    private ChatController chatController;

    public void registerChatController(@NonNull final ChatController chatController) {
        this.chatController = chatController;
    }

    @Override
    public void handle(@NonNull final String serverMessage) {
        if (chatController != null) {
            try {
                final ObjectNode json = new ObjectMapper().readValue(serverMessage, ObjectNode.class);

                System.out.println(serverMessage.toLowerCase());

                //temporary workaround
                if (!json.has("channel") || !json.has("from") || !json.has("message")) {
                    return;
                }
                
                final String channel = json.get("channel").asText();
                final String from = json.get("from").asText();
                final String content = json.get("message").asText();

                final String internalChannel = channel.equals(ChatController.SERVER_PUBLIC_CHANNEL_NAME) ? ChatController.GENERAL_CHANNEL_NAME : '@' + from;

                chatController.receiveMessage(internalChannel, from, content);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Chat controller not registered");
        }
    }
}
