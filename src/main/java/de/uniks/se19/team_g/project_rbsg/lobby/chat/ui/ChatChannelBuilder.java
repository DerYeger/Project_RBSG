package de.uniks.se19.team_g.project_rbsg.lobby.chat.ui;

import de.uniks.se19.team_g.project_rbsg.lobby.chat.ChatController;
import de.uniks.se19.team_g.project_rbsg.lobby.chat.ChatChannelController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import org.springframework.lang.NonNull;

import java.io.IOException;

/**
 * @author Jan Müller
 */
public class ChatChannelBuilder {

    private ChatController chatController;

    public ChatChannelBuilder(@NonNull final ChatController chatController) {
        this.chatController = chatController;
    }

    @NonNull
    public Node buildChatChannel(@NonNull final String channel) throws IOException {
        final FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(ChatChannelBuilder.class.getResource("chat-channel.fxml"));

        final Node chatTabContent = fxmlLoader.load();

        final ChatChannelController chatChannelController = fxmlLoader.getController();
        chatChannelController.init(chatController, channel);

        return chatTabContent;
    }
}
