package de.uniks.se19.team_g.project_rbsg.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.uniks.se19.team_g.project_rbsg.ViewComponent;
import de.uniks.se19.team_g.project_rbsg.bots.UserScopeBeanFactoryPostProcessor;
import de.uniks.se19.team_g.project_rbsg.chat.command.ChatCommandManager;
import de.uniks.se19.team_g.project_rbsg.chat.ui.ChatTabManager;
import de.uniks.se19.team_g.project_rbsg.lobby.chat.LobbyChatClient;
import de.uniks.se19.team_g.project_rbsg.server.websocket.IWebSocketCallback;
import de.uniks.se19.team_g.project_rbsg.server.websocket.WebSocketClient;
import de.uniks.se19.team_g.project_rbsg.chat.command.LeaveCommandHandler;
import de.uniks.se19.team_g.project_rbsg.chat.command.WhisperCommandHandler;
import de.uniks.se19.team_g.project_rbsg.chat.ui.ChatBuilder;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import javafx.scene.Node;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.Optional;

/**
 * @author Jan Müller
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        ChatControllerTests.ContextConfiguration.class,
        ChatCommandManager.class,
        ChatController.class,
        ChatBuilder.class,
        ChatTabManager.class,
        LobbyChatClient.class,
        UserProvider.class,
        UserScopeBeanFactoryPostProcessor.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
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
                public void onMessage(final String message, final Session session) {
                    if (callback != null) {
                        callback.handle(message);
                    }
                }

                @Override
                public void onOpen(final Session session) {

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
    private UserProvider userProvider;

    @Autowired
    private ChatBuilder chatBuilder;

    @Autowired
    private ChatClient chatClient;

    @Override
    public void start(@NonNull final Stage stage) throws Exception {
        userProvider.get()
                .setName("username");

        final ViewComponent<ChatController> chatComponents = chatBuilder.buildChat(chatClient);

        Assert.assertNotNull(chatComponents);
        Assert.assertNotNull(chatComponents.getRoot());
        Assert.assertNotNull(chatComponents.getController());

        final Scene scene = new Scene(chatComponents.getRoot(), 400, 300);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void test() {
        final TextInputControl generalInput = lookup(".text-field").queryTextInputControl();
        Assert.assertNotNull(generalInput);

        final TextInputControl generalMessageArea = lookup(".text-area").queryTextInputControl();
        Assert.assertNotNull(generalMessageArea);

        final String text = "T1";
        final String expectedPublicMessage = "{\"channel\":\"all\",\"message\":\"T1\"}";

        clickOn(generalInput);
        write(text);

        press(KeyCode.ENTER);
        release(KeyCode.ENTER);

        Assert.assertEquals("", generalInput.getText());
        Assert.assertEquals(expectedPublicMessage, sentMessages.get(0));

        final String whisperCommand = "/" + WhisperCommandHandler.COMMAND + " \"a\" T2";
        final String expectedPrivateMessage = "{\"channel\":\"private\",\"to\":\"a\",\"message\":\"T2\"}";

        clickOn(generalInput);
        write(whisperCommand);

        press(KeyCode.ENTER);
        release(KeyCode.ENTER);

        Assert.assertEquals(expectedPrivateMessage, sentMessages.get(1));

        final Node test2ChatTab = lookup("@a").query();
        Assert.assertNotNull(test2ChatTab);

        final TextInputControl secondTabInput = lookup(".text-field").queryTextInputControl();
        Assert.assertNotNull(secondTabInput);

        final String leaveCommand = "/" + LeaveCommandHandler.COMMAND;

        clickOn(secondTabInput);
        write(leaveCommand);

        press(KeyCode.ENTER);
        release(KeyCode.ENTER);
        
        final Optional<Node> nullTab = lookup("@a").tryQuery();
        Assert.assertFalse(nullTab.isPresent());

        final String incomingPrivateMessage = "{\"channel\":\"private\",\"message\":\"T3\",\"from\":\"b\"}";

        chatClient.handle(incomingPrivateMessage);

        WaitForAsyncUtils.waitForFxEvents();

        final Node test3ChatTab = lookup("@b").query();
        Assert.assertNotNull(test3ChatTab);

        clickOn(test3ChatTab);

        chatClient.handle("{\"msg\":\"User b is not online\"}");

        final Node ct3Input = lookup(".text-field")
                .queryAll()
                .stream()
                .filter(Node::isDisabled)
                .findAny()
                .orElse(null);

        Assert.assertNotNull(ct3Input);

        Assert.assertEquals("inputField", ct3Input.getId());

        Assert.assertNotNull(lookup("System: User b is not online"));
    }
}
