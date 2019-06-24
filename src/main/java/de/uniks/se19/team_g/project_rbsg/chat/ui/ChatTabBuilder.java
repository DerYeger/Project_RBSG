package de.uniks.se19.team_g.project_rbsg.chat.ui;

import de.uniks.se19.team_g.project_rbsg.ViewComponent;
import de.uniks.se19.team_g.project_rbsg.chat.ChatController;
import de.uniks.se19.team_g.project_rbsg.util.Tuple;
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
    public Tuple<Tab, ChatTabController> buildChatTab(@NonNull final String channel) throws IOException {
        final ViewComponent<ChatChannelController> channelComponents = chatChannelBuilder.buildChatChannel(channel);

        final Tab tab = new Tab(channel, channelComponents.getRoot());



        final ChatTabController chatTabController = new ChatTabController();
        chatTabController.init(chatController, tab, channel);

        channelComponents
                .getController()
                .inTab(tab, chatTabController);

        return new Tuple<>(tab, chatTabController);
    }
}
