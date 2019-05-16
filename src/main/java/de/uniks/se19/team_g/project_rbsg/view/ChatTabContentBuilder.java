package de.uniks.se19.team_g.project_rbsg.view;

import de.uniks.se19.team_g.project_rbsg.controller.ChatController;
import de.uniks.se19.team_g.project_rbsg.controller.ChatTabContentController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import org.springframework.lang.NonNull;

import java.io.IOException;

public class ChatTabContentBuilder {

    private ChatController chatController;

    public ChatTabContentBuilder(@NonNull final ChatController chatController) {
        this.chatController = chatController;
    }

    public Node buildChatTabContent(@NonNull final String chatPartner) throws IOException {
        final FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(LoginFormBuilder.class.getResource("chat-tab-content.fxml"));

        final Node chatTabContent = fxmlLoader.load();

        final ChatTabContentController chatTabContentController = fxmlLoader.getController();
        chatTabContentController.init(chatController, chatPartner);

        return chatTabContent;
    }
}
