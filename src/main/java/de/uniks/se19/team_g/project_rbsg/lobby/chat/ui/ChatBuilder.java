package de.uniks.se19.team_g.project_rbsg.lobby.chat.ui;

import de.uniks.se19.team_g.project_rbsg.lobby.chat.ChatController;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.TabPane;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Jan Müller
 */

@Component
@Scope("prototype")
public class ChatBuilder implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    private ChatController chatController;

    public ChatController getChatController() {
        return this.chatController;
    }

    @NonNull
    public Node buildChat() throws IOException {
        final TabPane chat = new TabPane();
        chat.setSide(Side.BOTTOM);
        chat.getStylesheets().add(this.getClass().getResource("/ui/lobby/chat/chat-channel.css").toExternalForm());
        if (chatController != null) {
            chatController.terminate();
        }

        chatController = applicationContext.getBean(ChatController.class);
        chatController.init(chat);

        return chat;
    }

    @Override
    public void setApplicationContext(@NonNull final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
