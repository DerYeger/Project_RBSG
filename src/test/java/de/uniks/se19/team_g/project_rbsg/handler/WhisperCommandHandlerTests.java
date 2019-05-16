package de.uniks.se19.team_g.project_rbsg.handler;

import de.uniks.se19.team_g.project_rbsg.controller.ChatController;
import de.uniks.se19.team_g.project_rbsg.controller.ChatTabContentController;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.lang.NonNull;

import java.util.HashSet;

/**
 * @author Jan Müller
 */
public class WhisperCommandHandlerTests {

    @Test
    public void testCorrectCommand() throws Exception {
        final HashSet<String> activeChannels = new HashSet<>();

        final ChatController chatController = new ChatController() {
            @Override
            public boolean addPrivateTab(@NonNull final String channel) {
                if (!activeChannels.contains(channel)) {
                    activeChannels.add(channel);
                    return true;
                }
                return false;
            }
        };

        final ChatTabContentController callback = new ChatTabContentController() {
            @Override
            public void displayMessage(@NonNull final String from, @NonNull final String content) {
                Assert.fail();
            }
        };

        final ChatCommandHandler handler = new WhisperCommandHandler(chatController);

        String[] options = {"channelName", "ignoredCommand"};

        Assert.assertTrue(handler.handleCommand(callback, "w", options));
        Assert.assertEquals(1, activeChannels.size());
        Assert.assertTrue(activeChannels.contains("channelName"));
    }


    @Test
    public void testWrongCommand() throws Exception {
        final HashSet<String> activeChannels = new HashSet<>();

        final ChatController chatController = new ChatController() {
            @Override
            public boolean addPrivateTab(@NonNull final String channel) {
                if (!activeChannels.contains(channel)) {
                    activeChannels.add(channel);
                    return true;
                }
                return false;
            }
        };

        final ChatTabContentController callback = new ChatTabContentController() {
            @Override
            public void displayMessage(@NonNull final String from, @NonNull final String content) {
                Assert.fail();
            }
        };

        final ChatCommandHandler handler = new WhisperCommandHandler(chatController);

        String[] options = {"channelName", "ignoredCommand"};

        Assert.assertFalse(handler.handleCommand(callback, "why", options));
        Assert.assertTrue(activeChannels.isEmpty());
    }

    @Test
    public void testWrongOptions() throws Exception {
        int[] optionErrorCount = {0};

        final HashSet<String> activeChannels = new HashSet<>();

        final ChatController chatController = new ChatController() {
            @Override
            public boolean addPrivateTab(@NonNull final String channel) {
                if (!activeChannels.contains(channel)) {
                    activeChannels.add(channel);
                    return true;
                }
                return false;
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

        final String[] firstTestOptions = {};
        final String[] secondTestOptions = null;

        Assert.assertTrue(handler.handleCommand(callback, "w", firstTestOptions));
        Assert.assertTrue(activeChannels.isEmpty());

        Assert.assertTrue(handler.handleCommand(callback, "w", secondTestOptions));
        Assert.assertTrue(activeChannels.isEmpty());

        Assert.assertEquals(2, optionErrorCount[0]);
    }

    @Test
    public void testDifferentChannels() throws Exception {
        int[] channelErrorCount = {0};

        final HashSet<String> activeChannels = new HashSet<>();

        final ChatController chatController = new ChatController() {
            @Override
            public boolean addPrivateTab(@NonNull final String channel) {
                if (!activeChannels.contains(channel)) {
                    activeChannels.add(channel);
                    return true;
                }
                return false;
            }
        };

        final ChatTabContentController callback = new ChatTabContentController() {
            @Override
            public void displayMessage(@NonNull final String from, @NonNull final String content) {
                Assert.assertEquals(WhisperCommandHandler.CHANNEL_ERROR_MESSAGE, content);
                channelErrorCount[0]++;
            }
        };

        final ChatCommandHandler handler = new WhisperCommandHandler(chatController);

        final String[] firstTestOptions = {"firstChannel"};
        final String[] secondTestOptions = {"secondChannel"};
        final String[] thirdTestOptions = {"firstChannel"};
        final String[] fourthTestOptions = {"secondChannel"};

        Assert.assertTrue(handler.handleCommand(callback, "w", firstTestOptions));
        Assert.assertEquals(1, activeChannels.size());
        Assert.assertEquals(0, channelErrorCount[0]);

        Assert.assertTrue(handler.handleCommand(callback, "w", secondTestOptions));
        Assert.assertEquals(2, activeChannels.size());
        Assert.assertEquals(0, channelErrorCount[0]);

        Assert.assertTrue(handler.handleCommand(callback, "w", thirdTestOptions));
        Assert.assertEquals(2, activeChannels.size());
        Assert.assertEquals(1, channelErrorCount[0]);

        Assert.assertTrue(activeChannels.contains("secondChannel"));
        activeChannels.remove("secondChannel");
        Assert.assertEquals(1, activeChannels.size());

        Assert.assertTrue(handler.handleCommand(callback, "w", fourthTestOptions));
        Assert.assertEquals(2, activeChannels.size());
        Assert.assertEquals(1, channelErrorCount[0]);
    }
}
