package de.uniks.se19.team_g.project_rbsg.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.uniks.se19.team_g.project_rbsg.server.websocket.IWebSocketCallback;
import de.uniks.se19.team_g.project_rbsg.server.websocket.WebSocketClient;
import de.uniks.se19.team_g.project_rbsg.chat.command.LeaveCommandHandler;
import de.uniks.se19.team_g.project_rbsg.chat.command.WhisperCommandHandler;
import de.uniks.se19.team_g.project_rbsg.chat.ui.ChatBuilder;
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
        ChatControllerTests.ContextConfiguration.class,
        ChatController.class,
        UserProvider.class,
        ChatWebSocketCallback.class,
        ChatBuilder.class
})
public class ChatControllerTests extends ApplicationTest {

    private static ArrayList<String> sentMessages = new ArrayList<>();

    @TestConfiguration
    static class ContextConfiguration {

        @Bean
        public WebSocketClient webSocketClient() {
            return new WebSocketClient() {

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
    private WebSocketClient webSocketClient;

    @Autowired
    private ChatWebSocketCallback chatWebSocketCallback;

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private ChatBuilder chatBuilder;

    @Override
    public void start(@NonNull final Stage stage) throws IOException {
        userProvider.get()
                .setName("chattest1");

        final Node chat = chatBuilder.buildChat();
        Assert.assertNotNull(chat);

        Assert.assertNotNull(chatBuilder.getChatController());

        webSocketClient.start("unimportant", chatWebSocketCallback);

        final Scene scene = new Scene((Parent) chat, 400, 300);
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

        clickOn(chattest3ChatTab);

        chatWebSocketCallback.handle("{\"msg\":\"User chattest3 is not online\"}");

        final Node ct3Input = lookup(".text-field")
                .queryAll()
                .stream()
                .filter(Node::isDisabled)
                .findAny()
                .orElse(null);

        Assert.assertNotNull(ct3Input);

        Assert.assertEquals("inputField", ct3Input.getId());

        Assert.assertNotNull(lookup("System: User chattest3 is not online"));
    }

    @Test
    public void testRecreation() throws IOException {
        final ChatController firstController = chatBuilder.getChatController();

        chatBuilder.buildChat(); //builds a new chat, thus the chatController reference is different

        final ChatController secondController = chatBuilder.getChatController();
        Assert.assertNotEquals(firstController, secondController);
    }
}
