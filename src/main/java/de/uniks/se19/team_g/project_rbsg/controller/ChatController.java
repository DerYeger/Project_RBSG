package de.uniks.se19.team_g.project_rbsg.controller;

import de.uniks.se19.team_g.project_rbsg.util.ChatCommandHandler;
import de.uniks.se19.team_g.project_rbsg.util.WhisperCommandHandler;
import de.uniks.se19.team_g.project_rbsg.view.ChatTabBuilder;
import de.uniks.se19.team_g.project_rbsg.view.ChatTabContentBuilder;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Jan Müller
 */
public class ChatController {

    public static final String SYSTEM = "System";

    private List<ChatCommandHandler> chatCommandHandlers;

    private HashMap<String, ChatTabContentController> activeChannels;

    private ChatTabBuilder chatTabBuilder;

    private TabPane chatPane;

    public void init(@NonNull final TabPane chatPane) throws IOException {
        this.chatPane = chatPane;

        final ChatTabContentBuilder chatTabContentBuilder = new ChatTabContentBuilder(this);
        chatTabBuilder = new ChatTabBuilder(chatTabContentBuilder, this);

        chatCommandHandlers = new ArrayList<>();
        activeChannels = new HashMap();

        chatCommandHandlers.add(new WhisperCommandHandler(this));

        addGeneralTab();
    }

    private void addGeneralTab() throws IOException {
        addTab("General", false);
    }

    public boolean addPrivateTab(@NonNull final String channel) throws IOException {
        return addTab(channel, true);
    }

    private boolean addTab(@NonNull final String channel, @NonNull final boolean isClosable) throws IOException {
        if (!activeChannels.containsKey(channel)) {
            final Tab tab = chatTabBuilder.buildChatTab(channel);
            chatPane.getTabs().add(tab);
            tab.setClosable(isClosable);
            chatPane.getSelectionModel().select(tab);
            return true;
        }
        return false;
    }

    //use this as an extensionpoint for chat commands
    public void handleInput(@NonNull final ChatTabContentController callback, @NonNull final String channel, @NonNull final String content) throws Exception {
        if (content.substring(0, 1).equals("/")) { //chat command detected
            if (content.length() < 2 || !handleCommand(callback, content.substring(1))) { //command could not be handled
                callback.displayMessage(SYSTEM, "Unknown or incorrect chat command");
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
        final String[] options = getCommandOptionsAsArray(content.substring(indexOfFirstOption));

        for (final ChatCommandHandler handler : chatCommandHandlers) {
            if (handler.handleCommand(callback, command, options)) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    private String[] getCommandOptionsAsArray(@Nullable final String options) {
        if (options == null || options.isBlank()) {
            return null;
        }


        return options.trim().split("\\s+");
    }

    //send the message to the server
    public void sendMessage(@NonNull final ChatTabContentController callback, @NonNull final String channel, @NonNull final String content) {
        //TODO implement sending message to the server

        //maybe replace You with user.getName() once I have access to the model
        callback.displayMessage("You", content);
    }

    //private channels will provide channel == from
    public void passMessage(@NonNull final String channel, @NonNull final String from, @NonNull final String content) throws IOException {
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
