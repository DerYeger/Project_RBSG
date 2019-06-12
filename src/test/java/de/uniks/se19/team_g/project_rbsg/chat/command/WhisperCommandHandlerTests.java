package de.uniks.se19.team_g.project_rbsg.chat.command;

import de.uniks.se19.team_g.project_rbsg.server.websocket.IWebSocketCallback;
import de.uniks.se19.team_g.project_rbsg.server.websocket.WebSocketClient;
import de.uniks.se19.team_g.project_rbsg.chat.ChatController;
import de.uniks.se19.team_g.project_rbsg.chat.ChatChannelController;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import javafx.scene.control.Tab;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.websocket.Session;
import java.io.IOException;
import java.util.HashSet;

/**
 * @author Jan Müller
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        WhisperCommandHandlerTests.ContextConfiguration.class,
        UserProvider.class,
})
public class WhisperCommandHandlerTests {

    private static final HashSet<String> activeChannels = new HashSet<>();


    @TestConfiguration
    static class ContextConfiguration {

        @Autowired
        private UserProvider userProvider;

        @Bean
        public WebSocketClient webSocketClient() {
            return new WebSocketClient() {
                @Override
                public void start(final @NonNull String endpoint, final @NonNull IWebSocketCallback callback) {
                    //do nothing
                }

                @Override
                public void onOpen(final Session session) throws IOException {

                }

                @Override
                public void sendMessage(final Object message) {
                    Assert.fail();
                }
            };
        }

        @Bean
        public ChatController chatController() {
            return new ChatController(userProvider, webSocketClient()) {
                @Override
                public Tab addPrivateTab(@NonNull final String channel) {
                    activeChannels.add(channel);
                    return null;
                }

                @Override
                public void receiveMessage(@NonNull final String channel, @NonNull final String from, @NonNull final String content) {
                    Assert.fail();
                }
            };
        }
    }

    @Autowired
    private ChatController chatController;

    @Autowired
    private UserProvider userProvider;

    private int[] optionErrorCount = {0, 0};

    @Test
    public void testWrongOptions() throws Exception {
        userProvider.get()
                .setName("MyUserName");

        final ChatChannelController callback = new ChatChannelController() {
            @Override
            public void displayMessage(@NonNull final String from, @NonNull final String content) {
                if (content.equals(WhisperCommandHandler.OPTION_ERROR_MESSAGE)) {
                    optionErrorCount[0]++;
                } else if (content.equals(WhisperCommandHandler.USER_ERROR_MESSAGE)) {
                    optionErrorCount[1]++;
                } else {
                    Assert.fail();
                }
            }
        };

        System.out.println(chatController);
        final ChatCommandHandler handler = new WhisperCommandHandler(chatController);

        final String firstTestOptions = "\"channel name\"";
        final String secondTestOptions = "\"\" message";
        final String thirdTestOptions = "";
        final String fourthTestOptions = null;
        final String fifthTestOptions = "\"channel name\"message";
        final String sixthTestOptions = "\"MyUserName\" message";

        handler.handleCommand(callback, firstTestOptions);
        Assert.assertTrue(activeChannels.isEmpty());

        handler.handleCommand(callback, secondTestOptions);
        Assert.assertTrue(activeChannels.isEmpty());

        handler.handleCommand(callback, thirdTestOptions);
        Assert.assertTrue(activeChannels.isEmpty());

        handler.handleCommand(callback, fourthTestOptions);
        Assert.assertTrue(activeChannels.isEmpty());

        handler.handleCommand(callback, fifthTestOptions);
        Assert.assertTrue(activeChannels.isEmpty());
        handler.handleCommand(callback, sixthTestOptions);
        Assert.assertTrue(activeChannels.isEmpty());

        Assert.assertEquals(5, optionErrorCount[0]);
        Assert.assertEquals(1, optionErrorCount[1]);
    }
}
