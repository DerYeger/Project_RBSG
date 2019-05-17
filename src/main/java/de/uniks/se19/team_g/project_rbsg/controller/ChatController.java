package de.uniks.se19.team_g.project_rbsg.controller;

import de.uniks.se19.team_g.project_rbsg.handler.ChatCommandHandler;
import de.uniks.se19.team_g.project_rbsg.handler.WhisperCommandHandler;
import de.uniks.se19.team_g.project_rbsg.view.ChatTabBuilder;
import de.uniks.se19.team_g.project_rbsg.view.ChatTabContentBuilder;
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

    private HashMap<String, ChatTabContentController> activeChannels;

    private ChatTabBuilder chatTabBuilder;

    private TabPane chatPane;

    public void init(@NonNull final TabPane chatPane) throws IOException {
        this.chatPane = chatPane;

        final ChatTabContentBuilder chatTabContentBuilder = new ChatTabContentBuilder(this);
        chatTabBuilder = new ChatTabBuilder(chatTabContentBuilder, this);

        chatCommandHandlers = new HashMap<>();
        activeChannels = new HashMap<>();

        addChatCommandHandlers();

        addGeneralTab();
    }

    private void addChatCommandHandlers() {
        chatCommandHandlers.put(WhisperCommandHandler.COMMAND, new WhisperCommandHandler(this));
    }

    private void addGeneralTab() throws IOException {
        addTab(GENERAL_CHANNEL_NAME, false);
    }

    public void addPrivateTab(@NonNull final String channel) throws IOException {
        addTab(channel, true);
    }

    private void addTab(@NonNull final String channel, @NonNull final boolean isClosable) throws IOException {
        if (!activeChannels.containsKey(channel)) {
            final Tab tab = chatTabBuilder.buildChatTab(channel);
            chatPane.getTabs().add(tab);
            tab.setClosable(isClosable);
            chatPane.getSelectionModel().select(tab);
        }
    }

    //use this as an extensionpoint for chat commands
    public void handleInput(@NonNull final ChatTabContentController callback, @NonNull final String channel, @NonNull final String content) throws Exception {
        if (content.substring(0, 1).equals("/")) { //chat command detected
            if (content.length() < 2 || !handleCommand(callback, content.substring(1))) { //command could not be handled
                callback.displayMessage(SYSTEM, "Unknown chat command");
            }
        } else { //send message
            sendMessage(callback, channel, content);
        }
    }

    private boolean handleCommand(@NonNull final ChatTabContentController callback, @NonNull final String content) throws Exception {
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
    public void sendMessage(@NonNull final ChatTabContentController callback, @NonNull final String channel, @NonNull final String content) throws IOException {
        //TODO implement sending message to the server

        receiveMessage(channel, "You", content);
    }

    //private channels will provide channel == from
    public void receiveMessage(@NonNull final String channel, @NonNull final String from, @NonNull final String content) throws IOException {
        if (!activeChannels.containsKey(channel)) {
            addPrivateTab(channel);
        }
        final ChatTabContentController chatTabContentController = activeChannels.get(channel);
        chatTabContentController.displayMessage(from, content);
    }

    //remove closed tabs
    public void removeChannelEntry(@NonNull final String channel) {
        activeChannels.remove(channel);
    }

    public void subscribeChatTabContentController(@NonNull final ChatTabContentController chatTabContentController, @NonNull final String channel) {
        activeChannels.put(channel, chatTabContentController);
    }
}
