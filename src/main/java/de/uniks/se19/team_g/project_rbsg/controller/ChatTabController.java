package de.uniks.se19.team_g.project_rbsg.controller;

import javafx.scene.control.Tab;
import org.springframework.lang.NonNull;

public class ChatTabController {

    private Tab chatTab;

    public void init(@NonNull final Tab chatTab, @NonNull final ChatController chatController) {
        this.chatTab = chatTab;
        chatTab.setOnCloseRequest(event -> chatController.removeTab(chatTab));
    }
}
