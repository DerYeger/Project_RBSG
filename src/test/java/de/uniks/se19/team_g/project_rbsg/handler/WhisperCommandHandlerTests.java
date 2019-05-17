package de.uniks.se19.team_g.project_rbsg.handler;

import de.uniks.se19.team_g.project_rbsg.controller.ChatController;
import de.uniks.se19.team_g.project_rbsg.controller.ChatTabContentController;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.util.HashSet;

/**
 * @author Jan Müller
 */
public class WhisperCommandHandlerTests {

    @Test
    public void testCorrectCommand() throws Exception {
        boolean[] messageReceived = {false};

        final HashSet<String> activeChannels = new HashSet<>();

        final ChatController chatController = new ChatController() {
            @Override
            public void addPrivateTab(@NonNull final String channel) throws IOException {
                if (!activeChannels.contains(channel)) {
                    activeChannels.add(channel);
                }
            }

            @Override
            public void sendMessage(@NonNull final ChatTabContentController callback, @NonNull final String channel, @NonNull final String content) throws IOException {
                receiveMessage(channel, "You", content);
            }

            @Override
            public void receiveMessage(@NonNull final String channel, @NonNull final String from, @NonNull final String content) throws IOException {
                if (!activeChannels.contains(channel)) {
                    addPrivateTab(channel);
                }
                messageReceived[0] = true;
            }
        };

        final ChatTabContentController callback = new ChatTabContentController() {
            @Override
            public void displayMessage(@NonNull final String from, @NonNull final String content) {
                messageReceived[0] = true;
            }
        };

        final ChatCommandHandler handler = new WhisperCommandHandler(chatController);

        final String options = "\"channel name\" message";

        handler.handleCommand(callback, options);
        Assert.assertEquals(1, activeChannels.size());
        Assert.assertTrue(activeChannels.contains("@channel name"));

        Assert.assertTrue(messageReceived[0]);
    }

    @Test
    public void testWrongOptions() throws Exception {
        int[] optionErrorCount = {0};

        final HashSet<String> activeChannels = new HashSet<>();

        final ChatController chatController = new ChatController() {
            @Override
            public void addPrivateTab(@NonNull final String channel) {
                if (!activeChannels.contains(channel)) {
                    activeChannels.add(channel);
                }
            }

            @Override
            public void receiveMessage(@NonNull final String channel, @NonNull final String from, @NonNull final String content) throws IOException {
                Assert.fail();
            }
        };

        final ChatTabContentController callback = new ChatTabContentController() {
            @Override
            public void displayMessage(@NonNull final String from, @NonNull final String content) {
                Assert.assertEquals(WhisperCommandHandler.OPTION_ERROR_MESSAGE, content);
                optionErrorCount[0]++;
            }
        };

        final ChatCommandHandler handler = new WhisperCommandHandler(chatController);

        final String firstTestOptions = "\"channel name\"";
        final String secondTestOptions = "\"\" message";
        final String thirdTestOptions = "";
        final String fourthTestOptions = null;
        final String fifthTestOptions = "\"channel name\"message";

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

        Assert.assertEquals(5, optionErrorCount[0]);
    }
}
