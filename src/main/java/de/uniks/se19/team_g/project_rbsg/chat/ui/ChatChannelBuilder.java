package de.uniks.se19.team_g.project_rbsg.chat.ui;

import de.uniks.se19.team_g.project_rbsg.ViewComponent;
import de.uniks.se19.team_g.project_rbsg.chat.ChatController;
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
    public ViewComponent<ChatChannelController> buildChatChannel(@NonNull final String channel) throws IOException {
        final FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(ChatChannelBuilder.class.getResource("/ui/chat/chat.fxml"));

        final Node chatTabContent = fxmlLoader.load();

        final ChatChannelController chatChannelController = fxmlLoader.getController();
        chatChannelController.init(chatController, channel);

        return new ViewComponent<>(chatTabContent, chatChannelController);
    }
}
