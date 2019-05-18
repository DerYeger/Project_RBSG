package de.uniks.se19.team_g.project_rbsg.controller;

import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.Contract.IWebSocketCallback;
import org.springframework.lang.NonNull;

import java.io.IOException;

/**
 * @author Jan Müller
 */
public class ChatWebSocketCallback implements IWebSocketCallback {

    private ChatController chatController;

    public void registerChatController(@NonNull final ChatController chatController) {
        this.chatController = chatController;
    }

    //TODO find out what the server actually send and implement the method correctly
    @Override
    public void handle(@NonNull final String serverMessage) {
        if (chatController != null) {
            try {
                chatController.receiveMessage(ChatController.GENERAL_CHANNEL_NAME, ChatController.SYSTEM, serverMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Chat controller not registered");
        }
    }
}
