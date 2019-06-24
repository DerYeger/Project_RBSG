package de.uniks.se19.team_g.project_rbsg.chat.ui;

import de.uniks.se19.team_g.project_rbsg.chat.ChatController;
import javafx.application.Platform;
import javafx.scene.control.Tab;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import org.springframework.lang.NonNull;

/**
 * @author Jan Müller
 */
public class ChatTabController {

    private Tab tab;

    public void init(@NonNull final ChatController chatController, @NonNull final Tab tab, @NonNull final String channel) {
        this.tab = tab;

        tab.setOnCloseRequest(event -> chatController.closeChannel(channel));
        tab.setOnSelectionChanged(event -> markRead());
    }

    public void markUnread() {
        if (!tab.isSelected()) {
            final Circle circle = new Circle(5);
            circle.setFill(Paint.valueOf("#03dac5"));
            Platform.runLater(() -> tab.setGraphic(circle));
        }
    }

    public void markRead() {
        Platform.runLater(() -> tab.setGraphic(null));
    }
}
