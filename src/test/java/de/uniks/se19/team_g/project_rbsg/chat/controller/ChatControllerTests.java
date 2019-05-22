package de.uniks.se19.team_g.project_rbsg.chat.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.uniks.se19.team_g.project_rbsg.JavaConfig;
import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.Contract.IWebSocketCallback;
import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.WebSocketClient;
import de.uniks.se19.team_g.project_rbsg.chat.handler.LeaveCommandHandler;
import de.uniks.se19.team_g.project_rbsg.chat.handler.WhisperCommandHandler;
import de.uniks.se19.team_g.project_rbsg.chat.view.ChatBuilder;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import javax.websocket.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author Jan Müller
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        JavaConfig.class,
        ChatControllerTests.ContextConfiguration.class,
        ChatController.class,
        UserProvider.class,
        ChatWebSocketCallback.class
})
public class ChatControllerTests extends ApplicationTest {

    private static ArrayList<String> sentMessages = new ArrayList<>();

    @TestConfiguration
    static class ContextConfiguration {

        @Bean
        public WebSocketClient webSocketClient() {
            return new WebSocketClient(null) {

                private IWebSocketCallback callback;

                @Override
                public void start(final @NonNull String endpoint, final @NonNull IWebSocketCallback callback) {
                    this.callback = callback;
                }

                @Override
                public void onMessage(final String message, final Session session) throws IOException {
                    if (callback != null) {
                        callback.handle(message);
                    }
                }

                @Override
                public void onOpen(final Session session) throws IOException {

                }

                @Override
                public void sendMessage(final Object message) {
                    try {
                        sentMessages.add(new ObjectMapper().writeValueAsString(message));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                        Assert.fail();
                    }

                }
            };
        }
    }

    @Autowired
    private ChatController chatController;

    @Autowired
    private WebSocketClient webSocketClient;

    @Autowired
    private ChatWebSocketCallback chatWebSocketCallback;

    @Autowired
    private UserProvider userProvider;

    @Override
    public void start(@NonNull final Stage stage) throws IOException {
        userProvider.getUser()
                .setName("chattest1");

        final ChatBuilder chatBuilder = new ChatBuilder(chatController);
        final Node chat = chatBuilder.getChat();
        Assert.assertNotNull(chat);

        webSocketClient.start("unimportant", chatWebSocketCallback);

        final Scene scene = new Scene((Parent) chat);
        stage.setWidth(400);
        stage.setHeight(600);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void test() throws IOException {
        final TextInputControl generalInput = lookup(".text-field").queryTextInputControl();
        Assert.assertNotNull(generalInput);

        final TextInputControl generalMessageArea = lookup(".text-area").queryTextInputControl();
        Assert.assertNotNull(generalMessageArea);

        final String text = "This is a test";
        final String expectedPublicMessage = "{\"channel\":\"all\",\"message\":\"This is a test\"}";

        clickOn(generalInput);
        write(text);

        press(KeyCode.ENTER);
        release(KeyCode.ENTER);

        Assert.assertEquals("", generalInput.getText());
        Assert.assertEquals(expectedPublicMessage, sentMessages.get(0));

        final String whisperCommand = "/" + WhisperCommandHandler.COMMAND + " \"chattest2\" Hello there!";
        final String expectedPrivateMessage = "{\"channel\":\"private\",\"to\":\"chattest2\",\"message\":\"Hello there!\"}";

        clickOn(generalInput);
        write(whisperCommand);

        press(KeyCode.ENTER);
        release(KeyCode.ENTER);

        Assert.assertEquals(expectedPrivateMessage, sentMessages.get(1));

        final Node chattest2ChatTab = lookup("@chattest2").query();
        Assert.assertNotNull(chattest2ChatTab);

        final TextInputControl secondTabInput = lookup(".text-field").queryTextInputControl();
        Assert.assertNotNull(secondTabInput);

        final String leaveCommand = "/" + LeaveCommandHandler.COMMAND;

        clickOn(secondTabInput);
        write(leaveCommand);

        press(KeyCode.ENTER);
        release(KeyCode.ENTER);
        
        final Optional<Node> nullTab = lookup("@chattest2").tryQuery();
        Assert.assertFalse(nullTab.isPresent());

        final String incomingPrivateMessage = "{\"channel\":\"private\",\"message\":\"The last test!\",\"from\":\"chattest3\"}";

        webSocketClient.onMessage(incomingPrivateMessage, null);

        //do not remove or the test will fail
        WaitForAsyncUtils.sleep(1, TimeUnit.SECONDS);

        final Node chattest3ChatTab = lookup("@chattest3").query();
        Assert.assertNotNull(chattest3ChatTab);
    }
}
