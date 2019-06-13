package de.uniks.se19.team_g.project_rbsg.chat.ui;

import de.uniks.se19.team_g.project_rbsg.chat.ChatController;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import org.springframework.lang.NonNull;

import java.io.IOException;

/**
 * @author Jan Müller
 */
public class ChatTabBuilder {

    private ChatChannelBuilder chatChannelBuilder;
    private ChatController chatController;

    public ChatTabBuilder(@NonNull final ChatChannelBuilder chatChannelBuilder, @NonNull final ChatController chatController) {
        this.chatChannelBuilder = chatChannelBuilder;
        this.chatController = chatController;
    }

    @NonNull
    public Tab buildChatTab(@NonNull final String channel) throws IOException {
        final Node chatTabContent = chatChannelBuilder.buildChatChannel(channel);

        final Tab newTab = new Tab(channel, chatTabContent);

        new ChatTabController().init(chatController, newTab, channel);

        return newTab;
    }
}
