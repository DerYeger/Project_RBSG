package de.uniks.se19.team_g.project_rbsg.view;

import de.uniks.se19.team_g.project_rbsg.controller.ChatController;
import javafx.scene.Node;
import javafx.scene.control.TabPane;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Jan Müller
 */

@Component
public class ChatBuilder {

    private TabPane chat;
    private ChatController chatController;

    public ChatController getChatController() {
        return this.chatController;
    }

    public ChatBuilder(@NonNull ChatController chatController) {
        this.chatController = chatController;
    }

    @NonNull
    public Node getChat() throws IOException {
        if (chat == null) {
            chat = new TabPane();
            chatController.init(chat);
        }
        return chat;
    }
}
