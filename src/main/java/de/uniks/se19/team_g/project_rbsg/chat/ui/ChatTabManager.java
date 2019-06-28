package de.uniks.se19.team_g.project_rbsg.chat.ui;

import de.uniks.se19.team_g.project_rbsg.chat.ChatController;
import de.uniks.se19.team_g.project_rbsg.util.Tuple;
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

    private HashMap<String, Tab> tabs;
    private HashMap<Tab, ChatTabController> tabControllers;

    public void init(@NonNull final ChatController chatController, @NonNull final TabPane tabPane, @NonNull final ChatTabBuilder chatTabBuilder) {
        this.chatController = chatController;
        this.tabPane = tabPane;
        this.chatTabBuilder = chatTabBuilder;

        tabs = new HashMap<>();
        tabControllers = new HashMap<>();

        addPublicTab();
    }

    private Tab addTab(@NonNull final String channel, @NonNull final boolean isClosable) {
        if (!tabs.containsKey(channel)) {
            try {
                final Tuple<Tab, ChatTabController> tabComponents = chatTabBuilder.buildChatTab(channel);
                final Tab tab = tabComponents.first;

                tab.setClosable(isClosable);

                tabs.put(channel, tab);
                tabControllers.put(tab, tabComponents.second);

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
        Tab tab;

        if (tabs.containsKey(channel)) {
            tab = tabs.get(channel);
        } else {
            tab = addPrivateTab(channel);
        }

        if (tab != null) {
            selectTab(tab);
        }
    }

    public void selectTab(@NonNull final String channel) {
        selectTab(tabs.get(channel));
    }

    public void selectTab(@NonNull final Tab tab) {
        tabControllers.get(tab).markRead();
        Platform.runLater(() -> tabPane.getSelectionModel().select(tab));
    }

    public boolean closeTab(@NonNull final String channel) {
        if (channel.equals(PUBLIC_CHANNEL_NAME)) {
            return false;
        } else if (tabs.containsKey(channel)) {
            tabPane.getTabs().remove(tabs.get(channel));
            tabControllers.remove(tabs.get(channel));
            tabs.remove(channel);
            return true;
        }
        return false;
    }
}
