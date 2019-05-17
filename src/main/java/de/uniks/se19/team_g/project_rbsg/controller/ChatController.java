package de.uniks.se19.team_g.project_rbsg.controller;

import de.uniks.se19.team_g.project_rbsg.handler.ChatCommandHandler;
import de.uniks.se19.team_g.project_rbsg.handler.LeaveCommandHandler;
import de.uniks.se19.team_g.project_rbsg.handler.WhisperCommandHandler;
import de.uniks.se19.team_g.project_rbsg.model.User;
import de.uniks.se19.team_g.project_rbsg.view.ChatTabBuilder;
import de.uniks.se19.team_g.project_rbsg.view.ChatChannelBuilder;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author Jan Müller
 */
public class ChatController {

    public static final String SYSTEM = "System";

    public static final String GENERAL_CHANNEL_NAME = "General";

    private HashMap<String, ChatCommandHandler> chatCommandHandlers;

    private HashMap<String, Tab> openChatTabs;

    private HashMap<String, ChatChannelController> openChatChannels;

    private ChatTabBuilder chatTabBuilder;

    private TabPane chatPane;

    private final User user;

    public ChatController(@NonNull final User user) {
        this.user = user;
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
            chatPane.getTabs().add(tab);
            tab.setClosable(isClosable);
            chatPane.getSelectionModel().select(tab);
            openChatTabs.put(channel, tab);
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
            sendMessage(callback, channel, content);
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

    //send the message to the server
    public void sendMessage(@NonNull final ChatChannelController callback, @NonNull final String channel, @NonNull final String content) throws IOException {
        //TODO implement sending message to the server

        receiveMessage(channel, "You", content);
        chatPane.getSelectionModel().select(openChatTabs.get(channel));
    }

    //private channels will provide channel == from
    public void receiveMessage(@NonNull final String channel, @NonNull final String from, @NonNull final String content) throws IOException {
        if (!openChatChannels.containsKey(channel)) {
            addPrivateTab(channel);
        }
        final ChatChannelController chatChannelController = openChatChannels.get(channel);
        chatChannelController.displayMessage(from, content);
    }

    //remove closed tabs
    public void removeChannelEntry(@NonNull final String channel) {
        openChatTabs.remove(channel);
        openChatChannels.remove(channel);
    }

    public void registerChatChannelController(@NonNull final ChatChannelController chatChannelController, @NonNull final String channel) {
        openChatChannels.put(channel, chatChannelController);
    }
}
