package de.uniks.se19.team_g.project_rbsg.chat.ui;

import de.uniks.se19.team_g.project_rbsg.chat.ChatClient;
import de.uniks.se19.team_g.project_rbsg.chat.ChatController;
import de.uniks.se19.team_g.project_rbsg.util.Tuple;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Region;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * @author Jan Müller
 */

@Component
@Scope("prototype")
public class ChatBuilder implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @NonNull
    public Tuple<Node, ChatController> buildChat(@NonNull final ChatClient chatClient) {
        final TabPane tabPane = new TabPane();
        tabPane.setSide(Side.BOTTOM);
        tabPane.getStylesheets().add(this.getClass().getResource("/ui/chat/chat.css").toExternalForm());

        final ChatController chatController = applicationContext.getBean(ChatController.class);

        tabPane.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);

        chatController.init(tabPane, chatClient);

        return new Tuple<>(tabPane, chatController);
    }

    @Override
    public void setApplicationContext(@NonNull final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
