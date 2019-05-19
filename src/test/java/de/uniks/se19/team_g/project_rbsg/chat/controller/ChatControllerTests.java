package de.uniks.se19.team_g.project_rbsg.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.RESTClient;
import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.WebSocketClient;
import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.WebSocketConfigurator;
import de.uniks.se19.team_g.project_rbsg.chat.handler.LeaveCommandHandler;
import de.uniks.se19.team_g.project_rbsg.chat.handler.WhisperCommandHandler;
import de.uniks.se19.team_g.project_rbsg.model.User;
import de.uniks.se19.team_g.project_rbsg.chat.view.ChatBuilder;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.lang.NonNull;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author Jan Müller
 */
public class ChatControllerTests extends ApplicationTest {

    private static final String FIRST_USER = "chattest1";
    private static final String SECOND_USER = "chattest2";

    @Override
    public void start(@NonNull final Stage stage) throws IOException {

        final Node firstChat = buildChatForUser(FIRST_USER);
        final Node secondChat = buildChatForUser(SECOND_USER);

        final Scene scene = new Scene(new VBox(firstChat, secondChat));
        stage.setWidth(400);
        stage.setHeight(600);
        stage.setScene(scene);
        stage.show();
    }

    private Node buildChatForUser(@NonNull final String userName) throws IOException {
        final RESTClient restClient = new RESTClient();
        String userKey = "nothing";
        final String loginResponse = restClient.post("/user/login", null, null, "{ \"name\" : \"" + userName + "\", \"password\" : \"" + userName + "\" }");
        System.out.println(userName);
        try
        {
            userKey = new ObjectMapper().readTree(loginResponse).get("data").get("userKey").asText();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        WebSocketConfigurator.userKey = userKey; //not sure if it breaks anything
        final User user = new User(userName, userName);

        final ChatWebSocketCallback webSocketCallback = new ChatWebSocketCallback();
        final WebSocketClient webSocketClient = new WebSocketClient("/chat?user=" + userName, webSocketCallback);
        final ChatController chatController = new ChatController(user, webSocketClient, webSocketCallback);

        final ChatBuilder chatBuilder = new ChatBuilder(chatController);
        final Node chat = chatBuilder.getChat();
        Assert.assertNotNull(chat);
        return chat;
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
        Assert.assertEquals("chattest1: " + text + '\n', generalMessageArea.getText());

        final String whisperCommand = "/" + WhisperCommandHandler.COMMAND + " \"chattest2\" Hello there!";

        clickOn(generalInput);
        write(whisperCommand);

        press(KeyCode.ENTER);
        release(KeyCode.ENTER);

        WaitForAsyncUtils.sleep(3, TimeUnit.SECONDS);

        final Node firstChatTab = lookup("@chattest2").query();
        Assert.assertNotNull(firstChatTab);

        final Node secondChatTab = lookup("@chattest1").query();
        Assert.assertNotNull(secondChatTab);

        final TextInputControl secondTabInput = lookup(".text-field").queryTextInputControl();
        Assert.assertNotNull(secondTabInput);

        final String leaveCommand = "/" + LeaveCommandHandler.COMMAND;

        clickOn(secondTabInput);
        write(leaveCommand);

        press(KeyCode.ENTER);
        release(KeyCode.ENTER);
        
        final Optional<Node> nullTab = lookup("@chattest2").tryQuery();
        Assert.assertFalse(nullTab.isPresent());
    }
}
