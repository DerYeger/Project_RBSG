package de.uniks.se19.team_g.project_rbsg.chat.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.RESTClient;
import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.WebSocketClient;
import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.WebSocketConfigurator;
import de.uniks.se19.team_g.project_rbsg.chat.controller.ChatController;
import de.uniks.se19.team_g.project_rbsg.chat.controller.ChatChannelController;
import de.uniks.se19.team_g.project_rbsg.chat.controller.ChatWebSocketCallback;
import de.uniks.se19.team_g.project_rbsg.chat.handler.WhisperCommandHandler;
import de.uniks.se19.team_g.project_rbsg.model.User;
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
//        boolean[] messageReceived = {false};
//
//        final HashSet<String> activeChannels = new HashSet<>();
//
//        final RESTClient restClient = new RESTClient();
//        String userKey = "nothing";
//        final String loginResponse = restClient.post("/user/login", null, null, "{ \"name\" : \"hello1\", \"password\" : \"hello1\" }");
//        try
//        {
//            userKey = new ObjectMapper().readTree(loginResponse).get("data").get("userKey").asText();
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//        WebSocketConfigurator.userKey = userKey;
//        final User user = new User("hello1", "hello1");
//
//        final ChatWebSocketCallback webSocketCallback = new ChatWebSocketCallback();
//        final WebSocketClient webSocketClient = new WebSocketClient("/chat?user=hello1", webSocketCallback);
//
//        final ChatController chatController = new ChatController(user, webSocketClient, webSocketCallback) {
//            @Override
//            public void addPrivateTab(@NonNull final String channel) throws IOException {
//                if (!activeChannels.contains(channel)) {
//                    activeChannels.add(channel);
//                }
//            }
//
//            @Override
//            public void sendMessage(@NonNull final ChatChannelController callback, @NonNull final String channel, @NonNull final String content) throws IOException {
//                receiveMessage(channel, "You", content);
//            }
//
//            @Override
//            public void receiveMessage(@NonNull final String channel, @NonNull final String from, @NonNull final String content) throws IOException {
//                if (!activeChannels.contains(channel)) {
//                    addPrivateTab(channel);
//                }
//                messageReceived[0] = true;
//            }
//        };
//
//        final ChatChannelController callback = new ChatChannelController() {
//            @Override
//            public void displayMessage(@NonNull final String from, @NonNull final String content) {
//                messageReceived[0] = true;
//            }
//        };
//
//        final ChatCommandHandler handler = new WhisperCommandHandler(chatController);
//
//        final String options = "\"channel name\" message";
//
//        handler.handleCommand(callback, options);
//        Assert.assertEquals(1, activeChannels.size());
//        Assert.assertTrue(activeChannels.contains("@channel name"));
//
//        Assert.assertTrue(messageReceived[0]);
    }

    @Test
    public void testWrongOptions() throws Exception {
//        int[] optionErrorCount = {0};
//
//        final HashSet<String> activeChannels = new HashSet<>();
//
//        final User user = new User("UserName", "1234");
//
//        final ChatWebSocketCallback webSocketCallback = new ChatWebSocketCallback();
//
//        final WebSocketClient webSocketClient = new WebSocketClient("/chat?user=" + user.getName(), webSocketCallback);
//
//        final ChatController chatController = new ChatController(user, webSocketClient, webSocketCallback) {
//            @Override
//            public void addPrivateTab(@NonNull final String channel) {
//                if (!activeChannels.contains(channel)) {
//                    activeChannels.add(channel);
//                }
//            }
//
//            @Override
//            public void receiveMessage(@NonNull final String channel, @NonNull final String from, @NonNull final String content) throws IOException {
//                Assert.fail();
//            }
//        };
//
//        final ChatChannelController callback = new ChatChannelController() {
//            @Override
//            public void displayMessage(@NonNull final String from, @NonNull final String content) {
//                Assert.assertEquals(WhisperCommandHandler.OPTION_ERROR_MESSAGE, content);
//                optionErrorCount[0]++;
//            }
//        };
//
//        final ChatCommandHandler handler = new WhisperCommandHandler(chatController);
//
//        final String firstTestOptions = "\"channel name\"";
//        final String secondTestOptions = "\"\" message";
//        final String thirdTestOptions = "";
//        final String fourthTestOptions = null;
//        final String fifthTestOptions = "\"channel name\"message";
//
//        handler.handleCommand(callback, firstTestOptions);
//        Assert.assertTrue(activeChannels.isEmpty());
//
//        handler.handleCommand(callback, secondTestOptions);
//        Assert.assertTrue(activeChannels.isEmpty());
//
//        handler.handleCommand(callback, thirdTestOptions);
//        Assert.assertTrue(activeChannels.isEmpty());
//
//        handler.handleCommand(callback, fourthTestOptions);
//        Assert.assertTrue(activeChannels.isEmpty());
//
//        handler.handleCommand(callback, fifthTestOptions);
//        Assert.assertTrue(activeChannels.isEmpty());
//
//        Assert.assertEquals(5, optionErrorCount[0]);
    }
}
