package de.uniks.se19.team_g.project_rbsg.chat;

import de.uniks.se19.team_g.project_rbsg.chat.command.*;
import de.uniks.se19.team_g.project_rbsg.chat.ui.ChatChannelBuilder;
import de.uniks.se19.team_g.project_rbsg.chat.ui.ChatChannelController;
import de.uniks.se19.team_g.project_rbsg.chat.ui.ChatTabBuilder;
import de.uniks.se19.team_g.project_rbsg.chat.ui.ChatTabManager;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import javafx.scene.control.TabPane;
import org.springframework.context.annotation.Scope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

/**
 * @author Jan Müller
 */
@Component
@Scope("prototype")
public class ChatController {

    public static final String SYSTEM = "System";

    public static final String CLIENT_PUBLIC_CHANNEL = "General";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private HashMap<String, ChatChannelController> chatChannelControllers;

    @NonNull
    private final UserProvider userProvider;

    private ChatClient chatClient;

    private ChatTabManager chatTabManager;

    private ChatCommandManager chatCommandManager;

    public ChatController(@NonNull final UserProvider userProvider, @NonNull final ChatCommandManager chatCommandManager, @NonNull final ChatTabManager chatTabManager)
    {
        this.userProvider = userProvider;
        this.chatCommandManager = chatCommandManager;
        this.chatTabManager = chatTabManager;
    }

    public void init(@NonNull final TabPane tabPane, @NonNull final ChatClient chatClient) {
        this.chatClient = chatClient;

        chatChannelControllers = new HashMap<>();

        final ChatChannelBuilder chatChannelBuilder = new ChatChannelBuilder(this);
        final ChatTabBuilder chatTabBuilder = new ChatTabBuilder(chatChannelBuilder, this);

        chatTabManager.init(this, tabPane, chatTabBuilder);

        addChatCommandHandlers();

        chatClient.startChatClient(this);
    }

    @NonNull
    public String getUserName() {
        return userProvider.get().getName();
    }

    @NonNull
    public ChatTabManager chatTabManager() {
        return chatTabManager;
    }

    //register additional chat command handlers in this method
    private void addChatCommandHandlers() {
        chatCommandManager.addHandler(new WhisperCommandHandler(this));
        chatCommandManager.addHandler(new LeaveCommandHandler(this));
        chatCommandManager.addHandler(new ChuckNorrisCommandHandler(this, new RestTemplate()));
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

        return chatCommandManager.handleCommand(callback, command, options);
    }

    public void sendMessage(@NonNull final ChatChannelController callback, @NonNull final String channel, @NonNull final String content) {
        String to = null;

        if (channel.charAt(0) == '@') { //private message
            to = channel.substring(1);
            receiveMessage(channel, userProvider.get().getName(), content);
        }

        chatClient.sendMessage(channel, to, content);

        chatTabManager.selectTab(channel);
    }

    public void receiveMessage(@NonNull final String channel, @NonNull final String from, @NonNull final String content) {
        if (!chatChannelControllers.containsKey(channel)) {
            chatTabManager.addPrivateTab(channel);
        }
        final ChatChannelController chatChannelController = chatChannelControllers.get(channel);
        chatChannelController.displayMessage(from, content.trim());
    }
    
    public void receiveErrorMessage(@NonNull final String message) {
        if (message.contains("User") && message.contains(" is not online")) {
            final int nameBeginIndex = message.indexOf("User") + 5;
            final int nameEndIndex = message.lastIndexOf(" is not online");
            final String channel = '@' + message.substring(nameBeginIndex, nameEndIndex);
            receiveMessage(channel, SYSTEM, message);
            chatChannelControllers.get(channel).disableInput();
        } else {
            receiveMessage(CLIENT_PUBLIC_CHANNEL, SYSTEM, message);
        }
    }

    public boolean closeChannel(@NonNull final String channel) {
        if (chatTabManager.closeTab(channel)) {
            chatChannelControllers.remove(channel);
            return true;
        }

        return false;
    }

    public void registerChatChannelController(@NonNull final ChatChannelController chatChannelController, @NonNull final String channel) {
        chatChannelControllers.put(channel, chatChannelController);
    }
}
