package de.uniks.se19.team_g.project_rbsg.controller;

import de.uniks.se19.team_g.project_rbsg.view.ChatTabBuilder;
import de.uniks.se19.team_g.project_rbsg.view.ChatTabContentBuilder;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.springframework.lang.NonNull;

import java.io.IOException;

public class ChatController {

    private ChatTabBuilder chatTabBuilder;
    private TabPane chatPane;

    public void init(@NonNull final TabPane chatPane) throws IOException {
        this.chatPane = chatPane;

        final ChatTabContentBuilder chatTabContentBuilder = new ChatTabContentBuilder(this);
        chatTabBuilder = new ChatTabBuilder(chatTabContentBuilder, this);

        addGeneralTab();
    }

    private void addGeneralTab() throws IOException {
        addTab("General", false);
    }

    private void addPrivateTab(@NonNull final String channel) throws IOException {
        addTab(channel, true);
    }

    private void addTab(@NonNull final String channel, @NonNull final boolean isClosable) throws IOException {
        final Tab tab = chatTabBuilder.buildChatTab(channel);
        chatPane.getTabs().add(tab);
        tab.setClosable(isClosable);
    }

    public void sendMessage(@NonNull final String channel, @NonNull final String content) {
        //TODO implement method
    }

    public void removeTab(@NonNull final Tab chatTab) {
        //TODO implement method
    }
}
