package de.uniks.se19.team_g.project_rbsg.view;

import de.uniks.se19.team_g.project_rbsg.controller.ChatController;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.lang.NonNull;
import org.testfx.framework.junit.ApplicationTest;

import java.io.IOException;

/**
 * @author Jan Müller
 */
public class ChatBuilderTests extends ApplicationTest {

    @Override
    public void start(@NonNull final Stage stage) throws IOException {
        final ChatBuilder chatBuilder = new ChatBuilder(new ChatController());
        final Node chat = chatBuilder.getChat();
        Assert.assertNotNull(chat);

        final Scene scene = new Scene((Parent) chat);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void test() {
        Assert.assertNotNull(lookup(ChatController.GENERAL_CHANNEL_NAME).query());
        Assert.assertNotNull(lookup(".text-area").query());
        Assert.assertNotNull(lookup(".text-field").query());
    }
}
