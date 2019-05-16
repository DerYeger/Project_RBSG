package de.uniks.se19.team_g.project_rbsg.view;

import de.uniks.se19.team_g.project_rbsg.controller.ChatController;
import de.uniks.se19.team_g.project_rbsg.controller.ChatTabController;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import org.springframework.lang.NonNull;

import java.io.IOException;

/**
 * @author Jan Müller
 */
public class ChatTabBuilder {

    private ChatTabContentBuilder chatTabContentBuilder;
    private ChatController chatController;

    public ChatTabBuilder(@NonNull final ChatTabContentBuilder chatTabContentBuilder, @NonNull final ChatController chatController) {
        this.chatTabContentBuilder = chatTabContentBuilder;
        this.chatController = chatController;
    }

    public Tab buildChatTab(@NonNull final String channel) throws IOException {
        final Node chatTabContent = chatTabContentBuilder.buildChatTabContent(channel);

        final Tab newTab = new Tab(channel.equals(ChatController.INTERNAL_GENERAL_CHANNEL_NAME) ? ChatController.GENERAL_CHANNEL_NAME : channel, chatTabContent);

        new ChatTabController().init(chatController, newTab, channel);

        return newTab;
    }
}
