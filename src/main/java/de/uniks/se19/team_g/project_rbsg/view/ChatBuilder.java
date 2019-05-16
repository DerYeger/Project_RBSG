package de.uniks.se19.team_g.project_rbsg.view;

import de.uniks.se19.team_g.project_rbsg.controller.ChatController;
import javafx.scene.Node;
import javafx.scene.control.TabPane;

import java.io.IOException;

/**
 * @author Jan Müller
 */
public class ChatBuilder {

    private TabPane chat;

    public Node getChat() throws IOException {
        if (chat == null) {
            chat = new TabPane();

            final ChatController chatController = new ChatController();
            chatController.init(chat);
        }
        return chat;
    }
}
