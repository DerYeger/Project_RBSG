package de.uniks.se19.team_g.project_rbsg.chat.view;

import de.uniks.se19.team_g.project_rbsg.chat.controller.ChatController;
import javafx.scene.Node;
import javafx.scene.control.TabPane;
import org.springframework.lang.NonNull;

import java.io.IOException;

/**
 * @author Jan Müller
 */
public class ChatBuilder {

    private TabPane chat;
    private ChatController chatController;

    public ChatBuilder(@NonNull final ChatController chatController) {
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
