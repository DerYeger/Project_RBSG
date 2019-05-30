package de.uniks.se19.team_g.project_rbsg.lobby.chat.ui;

import de.uniks.se19.team_g.project_rbsg.lobby.chat.ChatController;
import javafx.scene.control.Tab;
import org.springframework.lang.NonNull;

/**
 * @author Jan Müller
 */
public class ChatTabController {

    public void init(@NonNull final ChatController chatController, @NonNull final Tab chatTab, @NonNull final String channel) {
        chatTab.setOnCloseRequest(event -> chatController.removeChannelEntry(channel));
    }
}
