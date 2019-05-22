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

    private HashMap<String, Tab> openChatTabs;

    private HashMap<String, ChatChannelController> openChatChannels;

    private ChatTabBuilder chatTabBuilder;

    private TabPane chatPane;

    @NonNull
    private final UserProvider userProvider;

    @NonNull
    private final WebSocketClient webSocketClient;

    @NonNull
    private final ChatWebSocketCallback chatWebSocketCallback;

    public ChatController(@NonNull final UserProvider userProvider, @NonNull final WebSocketClient webSocketClient, @NonNull final ChatWebSocketCallback chatWebSocketCallback) {
        this.userProvider = userProvider;
        this.webSocketClient = webSocketClient;
        this.chatWebSocketCallback = chatWebSocketCallback;
    }

    public void init(@NonNull final TabPane chatPane) throws IOException {
        this.chatPane = chatPane;

        final ChatChannelBuilder chatChannelBuilder = new ChatChannelBuilder(this);
        chatTabBuilder = new ChatTabBuilder(chatChannelBuilder, this);

        chatCommandHandlers = new HashMap<>();
        openChatTabs = new HashMap<>();
        openChatChannels = new HashMap<>();

        addChatCommandHandlers();

        addGeneralTab();

        startClient();
    }

    private void startClient() {
        chatWebSocketCallback.registerChatController(this);
        webSocketClient.start(SERVER_ENDPOINT + userProvider.getUser().getName(), chatWebSocketCallback);
    }

    @NonNull
    public String getUserName() {
        return userProvider.getUser().getName();
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
        if (!openChatChannels.containsKey(channel)) {
            final Tab tab = chatTabBuilder.buildChatTab(channel);
            Platform.runLater(() -> {
                chatPane.getTabs().add(tab);
                tab.setClosable(isClosable);
                chatPane.getSelectionModel().select(tab);
                openChatTabs.put(channel, tab);
            });
        }
    }

    public boolean removeTab(@NonNull final String channel) {
        if (channel.equals(GENERAL_CHANNEL_NAME)) {
            return false;
        } else if (openChatTabs.containsKey(channel)) {
            chatPane.getTabs().remove(openChatTabs.get(channel));
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
        } else { //send message
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

    public void sendMessage(@NonNull final ChatChannelController callback, @NonNull final String channel, @NonNull final String content) throws IOException {
        try {
            final ObjectNode node = getMessageAsNode(channel, content);
            webSocketClient.sendMessage(node);

            if (!channel.equals(GENERAL_CHANNEL_NAME)) {
                receiveMessage(channel, userProvider.getUser().getName(), content);
            }

            Platform.runLater(() -> chatPane.getSelectionModel().select(openChatTabs.get(channel)));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to send message");
        }
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

    public void receiveMessage(@NonNull final String channel, @NonNull final String from, @NonNull final String content) throws IOException {
        if (!openChatChannels.containsKey(channel)) {
            addPrivateTab(channel);
        }
        final ChatChannelController chatChannelController = openChatChannels.get(channel);
        chatChannelController.displayMessage(from, content.trim());
    }

    public void removeChannelEntry(@NonNull final String channel) {
        openChatTabs.remove(channel);
        openChatChannels.remove(channel);
    }

    public void registerChatChannelController(@NonNull final ChatChannelController chatChannelController, @NonNull final String channel) {
        openChatChannels.put(channel, chatChannelController);
    }
}
