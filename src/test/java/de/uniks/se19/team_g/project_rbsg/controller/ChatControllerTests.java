package de.uniks.se19.team_g.project_rbsg.controller;

import de.uniks.se19.team_g.project_rbsg.view.ChatBuilder;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.lang.NonNull;
import org.testfx.framework.junit.ApplicationTest;

import java.io.IOException;

/**
 * @author Jan Müller
 */
public class ChatControllerTests extends ApplicationTest {

    private ChatController chatController;

    @Override
    public void start(@NonNull final Stage stage) throws IOException {
        chatController = new ChatController();
        final ChatBuilder chatBuilder = new ChatBuilder(chatController);
        final Node chat = chatBuilder.getChat();
        Assert.assertNotNull(chat);

        final Scene scene = new Scene((Parent) chat);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void test() {
        final TextInputControl generalInput = lookup(".text-field").queryTextInputControl();
        Assert.assertNotNull(generalInput);

        final TextInputControl generalMessageArea = lookup(".text-area").queryTextInputControl();
        Assert.assertNotNull(generalMessageArea);

        final String text = "This is a test";

        clickOn(generalInput);
        write(text);

        press(KeyCode.ENTER);
        release(KeyCode.ENTER);

        Assert.assertEquals("", generalInput.getText());
        Assert.assertEquals("You: " + text + '\n', generalMessageArea.getText());

        final String command = "/w \"Second Tab\" Hello there!";

        clickOn(generalInput);
        write(command);

        press(KeyCode.ENTER);
        release(KeyCode.ENTER);

        final Node newTab = lookup("@Second Tab").query();
        Assert.assertNotNull(newTab);
    }
}
