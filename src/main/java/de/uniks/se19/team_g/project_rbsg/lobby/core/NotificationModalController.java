package de.uniks.se19.team_g.project_rbsg.lobby.core;

import de.uniks.se19.team_g.project_rbsg.ProjectRbsgFXApplication;
import io.rincl.Resources;
import io.rincl.Rincl;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class NotificationModalController {
    public Button dismiss;
    public VBox inbox;

    public void setOnDismiss(BiConsumer<ActionEvent, NotificationModalController> onDismiss) {
        dismiss.setOnAction(event -> onDismiss.accept(event, this));
    }

    public void setNotifications(List<String> notifications) {
        final Resources resources = Rincl.getResources(ProjectRbsgFXApplication.class);

        final List<TextArea> notificationNodes = new ArrayList<>();

        for (final String notification : notifications) {
            notificationNodes.add(createNotification(notification, resources));
        }

        inbox.getChildren().setAll(notificationNodes);
    }

    private TextArea createNotification(String key, Resources resources) {
        final String text;
        if (resources.hasResource(key)) {
            text = resources.getString(key);
        } else {
            text = key;
        }

        final TextArea notificaton = new TextArea();
        notificaton.setEditable(false);
        notificaton.setPrefRowCount(0);
        notificaton.setWrapText(true);
        notificaton.setText(text);

        return notificaton;
    }
}
