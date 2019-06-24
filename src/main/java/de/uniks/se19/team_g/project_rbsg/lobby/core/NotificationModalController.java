package de.uniks.se19.team_g.project_rbsg.lobby.core;

import de.uniks.se19.team_g.project_rbsg.ProjectRbsgFXApplication;
import io.rincl.Resources;
import io.rincl.Rincl;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

@Component
public class NotificationModalController {
    public Button dismiss;
    public VBox inbox;

    public void setOnDismiss(BiConsumer<ActionEvent, NotificationModalController> onDismiss) {
        dismiss.setOnAction(event -> onDismiss.accept(event, this));
    }

    public void setNotifications(List<String> notifications) {
        final Resources resources = Rincl.getResources(ProjectRbsgFXApplication.class);

        final List<Label> notificationNodes = new ArrayList<>();

        for (final String notification : notifications) {
            notificationNodes.add(createNotification(notification, resources));
        }

        inbox.getChildren().setAll(notificationNodes);
    }

    private Label createNotification(String key, Resources resources) {
        final String text;
        if (resources.hasResource(key)) {
            text = resources.getString(key);
        } else {
            text = key;
        }

        final Label notificaton = new Label();
        notificaton.setWrapText(true);
        notificaton.setText(text);
        notificaton.getStyleClass().setAll("notification", "content");

        return notificaton;
    }
}
