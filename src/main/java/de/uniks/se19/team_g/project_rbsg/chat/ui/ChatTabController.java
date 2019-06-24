package de.uniks.se19.team_g.project_rbsg.chat.ui;

import de.uniks.se19.team_g.project_rbsg.chat.ChatController;
import javafx.scene.control.Tab;
import org.springframework.lang.NonNull;

/**
 * @author Jan Müller
 */
public class ChatTabController {

    private static final String UNREAD = "unread";

    private Tab tab;

    public void init(@NonNull final ChatController chatController, @NonNull final Tab tab, @NonNull final String channel) {
        this.tab = tab;
        tab.setOnCloseRequest(event -> chatController.closeChannel(channel));
        tab.setOnSelectionChanged(event -> markRead());
    }

    public void markUnread() {
        if (!tab.isSelected()) tab.getStyleClass().add(UNREAD);
    }

    public void markRead() {
        tab.getStyleClass().remove(UNREAD);
    }
}
