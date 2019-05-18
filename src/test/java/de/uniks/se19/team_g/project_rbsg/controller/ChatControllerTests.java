package de.uniks.se19.team_g.project_rbsg.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.RESTClient;
import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.WebSocketClient;
import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.WebSocketConfigurator;
import de.uniks.se19.team_g.project_rbsg.handler.LeaveCommandHandler;
import de.uniks.se19.team_g.project_rbsg.handler.WhisperCommandHandler;
import de.uniks.se19.team_g.project_rbsg.model.User;
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
import java.util.Optional;

/**
 * @author Jan Müller
 */
public class ChatControllerTests extends ApplicationTest {

    @Override
    public void start(@NonNull final Stage stage) throws IOException {
        final RESTClient restClient = new RESTClient();
        String userKey = "nothing";
        final String loginResponse = restClient.post("/user/login", null, null, "{ \"name\" : \"hello1\", \"password\" : \"hello1\" }");
        try
        {
            userKey = new ObjectMapper().readTree(loginResponse).get("data").get("userKey").asText();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        WebSocketConfigurator.userKey = userKey;
        final User user = new User("hello1", "hello1");

        final ChatWebSocketCallback webSocketCallback = new ChatWebSocketCallback() {
            @Override
            public void handle(@NonNull final String serverMessage) {

            }

        };
        final WebSocketClient webSocketClient = new WebSocketClient("/chat?user=hello1", webSocketCallback);
        final ChatController chatController = new ChatController(user, webSocketClient, webSocketCallback);

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

        final String whisperCommand = "/" + WhisperCommandHandler.COMMAND + " \"Second Tab\" Hello there!";

        clickOn(generalInput);
        write(whisperCommand);

        press(KeyCode.ENTER);
        release(KeyCode.ENTER);

        final Node newTab = lookup("@Second Tab").query();
        Assert.assertNotNull(newTab);

        final TextInputControl secondTabInput = lookup(".text-field").queryTextInputControl();
        Assert.assertNotNull(secondTabInput);

        final String leaveCommand = "/" + LeaveCommandHandler.COMMAND;

        clickOn(secondTabInput);
        write(leaveCommand);

        press(KeyCode.ENTER);
        release(KeyCode.ENTER);

        final Optional<Node> nullTab = lookup("@Second Tab").tryQuery();
        Assert.assertFalse(nullTab.isPresent());
    }
}
