package de.uniks.se19.team_g.project_rbsg.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.WebSocketClient;
import de.uniks.se19.team_g.project_rbsg.chat.handler.ChatCommandHandler;
import de.uniks.se19.team_g.project_rbsg.chat.handler.LeaveCommandHandler;
import de.uniks.se19.team_g.project_rbsg.chat.handler.WhisperCommandHandler;
import de.uniks.se19.team_g.project_rbsg.chat.view.ChatTabBuilder;
import de.uniks.se19.team_g.project_rbsg.chat.view.ChatChannelBuilder;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import javafx.application.Platform;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * @author Jan Müller
 */
@Component
public class ChatController {

    public static final String SYSTEM = "System";

    public static final String GENERAL_CHANNEL_NAME = "General";

    public static final String SERVER_PUBLIC_CHANNEL_NAME = "all";

    public static final String SERVER_PRIVATE_CHANNEL_NAME = "private";

    public static final String SERVER_ENDPOINT = "/chat?user=";

    private HashMap<String, ChatCommandHandler> chatCommandHandlers;

    private HashMap<String, Tab> chatTabs;

    private HashMap<String, ChatChannelController> chatChannelControllers;

    private ChatTabBuilder chatTabBuilder;

    private TabPane chatPane;

    @NonNull
    private final UserProvider userProvider;

    @NonNull
    private final WebSocketClient webSocketClient;

    @NonNull
    private final ChatWebSocketCallback chatWebSocketCallback;

    public ChatController(@NonNull final UserProvider userProvider, @NonNull final WebSocketClient webSocketClient, @NonNull final ChatWebSocketCallback chatWebSocketCallback)
    {
        this.userProvider = userProvider;
        this.webSocketClient = webSocketClient;
        this.chatWebSocketCallback = chatWebSocketCallback;
    }

    public void init(@NonNull final TabPane chatPane) throws IOException {
        this.chatPane = chatPane;

        final ChatChannelBuilder chatChannelBuilder = new ChatChannelBuilder(this);
        chatTabBuilder = new ChatTabBuilder(chatChannelBuilder, this);

        chatCommandHandlers = new HashMap<>();
        chatTabs = new HashMap<>();
        chatChannelControllers = new HashMap<>();

        addChatCommandHandlers();

        addGeneralTab();

        startClient();
    }

    private void startClient() throws UnsupportedEncodingException
    {
        chatWebSocketCallback.registerChatController(this);
        webSocketClient.start(SERVER_ENDPOINT + URLEncoder.encode(userProvider.get().getName(), StandardCharsets.UTF_8.name()), chatWebSocketCallback);
    }

    @NonNull
    public String getUserName() {
        return userProvider.get().getName();
    }

    //register additional chat command handlers in this method
    private void addChatCommandHandlers() {
        chatCommandHandlers.put(WhisperCommandHandler.COMMAND, new WhisperCommandHandler(this));
        chatCommandHandlers.put(LeaveCommandHandler.COMMAND, new LeaveCommandHandler(this));
    }

    private void addGeneralTab() throws IOException {
        addTab(GENERAL_CHANNEL_NAME, false);
    }

    public void addPrivateTab(@NonNull final String channel) throws IOException {
        addTab(channel, true);
    }

    private void addTab(@NonNull final String channel, @NonNull final boolean isClosable) throws IOException {
        if (!chatChannelControllers.containsKey(channel)) {
            final Tab tab = chatTabBuilder.buildChatTab(channel);
            Platform.runLater(() -> {
                chatPane.getTabs().add(tab);
                tab.setClosable(isClosable);
                chatTabs.put(channel, tab);
            });
        }
    }

    public boolean removeTab(@NonNull final String channel) {
        if (channel.equals(GENERAL_CHANNEL_NAME)) {
            return false;
        } else if (chatTabs.containsKey(channel)) {
            chatPane.getTabs().remove(chatTabs.get(channel));
            removeChannelEntry(channel);
            return true;
        }
        return false;
    }

    public void handleInput(@NonNull final ChatChannelController callback, @NonNull final String channel, @NonNull final String content) throws Exception {
        if (content.substring(0, 1).equals("/")) { //chat command detected
            if (content.length() < 2 || !handleCommand(callback, content.substring(1))) { //command could not be handled
                callback.displayMessage(SYSTEM, "Unknown chat command");
            }
        } else {
            sendMessage(callback, channel, content.trim());
        }
    }

    private boolean handleCommand(@NonNull final ChatChannelController callback, @NonNull final String content) throws Exception {
        if (content.isBlank()) {
            return false;
        }

        final int indexOfFirstOption = content.indexOf(' ') == -1 ? content.length() : content.indexOf(' ');

        final String command = content.substring(0, indexOfFirstOption);
        final String options = content.substring(indexOfFirstOption);

        if (chatCommandHandlers.containsKey(command)) {
            chatCommandHandlers.get(command).handleCommand(callback, options);
            return true;
        }
        return false;
    }

    public void sendMessage(@NonNull final ChatChannelController callback, @NonNull final String channel, @NonNull final String content) {
        final ObjectNode node = getMessageAsNode(channel, content);
        webSocketClient.sendMessage(node);

        if (channel.charAt(0) == '@') { //private message
            //TODO: Check if user is reachable first (via user list in lobby and game participants in game) later on
            receiveMessage(channel, userProvider.get().getName(), content);
        }

        Platform.runLater(() -> chatPane.getSelectionModel().select(chatTabs.get(channel)));
    }

    @NonNull
    private ObjectNode getMessageAsNode(@NonNull final String channel, @NonNull final String content) {
        final ObjectMapper objectMapper = new ObjectMapper();
        final ObjectNode node = objectMapper.createObjectNode();

        if (channel.equals(GENERAL_CHANNEL_NAME)) { //public message
            node.put("channel", SERVER_PUBLIC_CHANNEL_NAME);
        } else { //private message
            node.put("channel", SERVER_PRIVATE_CHANNEL_NAME);
            node.put("to", channel.substring(1));
        }

        node.put("message", content);

        return node;
    }

    public void receiveMessage(@NonNull final String channel, @NonNull final String from, @NonNull final String content) {
        try {
            if (!chatChannelControllers.containsKey(channel)) {
                addPrivateTab(channel);
            }
            final ChatChannelController chatChannelController = chatChannelControllers.get(channel);
            chatChannelController.displayMessage(from, content.trim());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void receiveErrorMessage(@NonNull final String message) {
        if (message.contains("User") && message.contains(" is not online")) {
            final int nameBeginIndex = message.indexOf("User") + 5;
            final int nameEndIndex = message.lastIndexOf(" is not online");
            final String channel = '@' + message.substring(nameBeginIndex, nameEndIndex);
            receiveMessage(channel, SYSTEM, message);
            chatChannelControllers.get(channel).disableInput();
        } else {
            receiveMessage(GENERAL_CHANNEL_NAME, SYSTEM, message);
        }
    }

    public void removeChannelEntry(@NonNull final String channel) {
        chatTabs.remove(channel);
        chatChannelControllers.remove(channel);
    }

    public void registerChatChannelController(@NonNull final ChatChannelController chatChannelController, @NonNull final String channel) {
        chatChannelControllers.put(channel, chatChannelController);
    }

    public void terminate() {
        webSocketClient.stop();
    }
}
