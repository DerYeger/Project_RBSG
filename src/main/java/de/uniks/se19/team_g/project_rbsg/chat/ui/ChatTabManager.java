package de.uniks.se19.team_g.project_rbsg.chat.ui;

import de.uniks.se19.team_g.project_rbsg.chat.ChatController;
import javafx.application.Platform;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author Jan Müller
 */
@Component
@Scope("prototype")
public class ChatTabManager {

    private static final String PUBLIC_CHANNEL_NAME = "General";

    @NonNull
    private ChatController chatController;

    @NonNull
    private TabPane tabPane;

    @NonNull
    private ChatTabBuilder chatTabBuilder;

    private HashMap<String, Tab> chatTabs;

    public void init(@NonNull final ChatController chatController, @NonNull final TabPane tabPane, @NonNull final ChatTabBuilder chatTabBuilder) {
        this.chatController = chatController;
        this.tabPane = tabPane;
        this.chatTabBuilder = chatTabBuilder;

        chatTabs = new HashMap<>();

        addPublicTab();
    }

    private Tab addTab(@NonNull final String channel, @NonNull final boolean isClosable) {
        if (!chatTabs.containsKey(channel)) {
            try {
                final Tab tab = chatTabBuilder.buildChatTab(channel);
                tab.setClosable(isClosable);
                chatTabs.put(channel, tab);
                Platform.runLater(() -> tabPane.getTabs().add(tab));
                return tab;
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void addPublicTab()  {
        addTab(PUBLIC_CHANNEL_NAME, false);
    }

    public Tab addPrivateTab(@NonNull final String channel) {
        return addTab(channel, true);
    }

    public void openTab(@NonNull final String channel) {
        Tab tab = null;

        if (chatTabs.containsKey(channel)) {
            tab = chatTabs.get(channel);
        } else {
            tab = addPrivateTab(channel);
        }

        if (tab != null) {
            selectTab(tab);
        }
    }

    public void selectTab(@NonNull final String channel) {
        selectTab(chatTabs.get(channel));
    }

    public void selectTab(@NonNull final Tab tab) {
        Platform.runLater(() -> tabPane.getSelectionModel().select(tab));
    }

    public boolean closeTab(@NonNull final String channel) {
        if (channel.equals(PUBLIC_CHANNEL_NAME)) {
            return false;
        } else if (chatTabs.containsKey(channel)) {
            tabPane.getTabs().remove(chatTabs.get(channel));
            chatTabs.remove(channel);
            return true;
        }
        return false;
    }
}
