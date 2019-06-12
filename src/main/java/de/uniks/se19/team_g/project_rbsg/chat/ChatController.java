package de.uniks.se19.team_g.project_rbsg.chat;

import de.uniks.se19.team_g.project_rbsg.chat.command.ChuckNorrisCommandHandler;
import de.uniks.se19.team_g.project_rbsg.chat.command.ChatCommandHandler;
import de.uniks.se19.team_g.project_rbsg.chat.command.LeaveCommandHandler;
import de.uniks.se19.team_g.project_rbsg.chat.command.WhisperCommandHandler;
import de.uniks.se19.team_g.project_rbsg.chat.ui.ChatChannelBuilder;
import de.uniks.se19.team_g.project_rbsg.chat.ui.ChatTabBuilder;
import de.uniks.se19.team_g.project_rbsg.chat.ui.ChatTabManager;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.termination.Terminable;
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
public class ChatController implements Terminable {

    public static final String SYSTEM = "System";

    public static final String GENERAL_CHANNEL_NAME = "General";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private HashMap<String, ChatCommandHandler> chatCommandHandlers;

    private HashMap<String, ChatChannelController> chatChannelControllers;

    @NonNull
    private final UserProvider userProvider;

    private ChatTabManager chatTabManager;

    private ChatClient chatClient;

    public ChatController(@NonNull final UserProvider userProvider)
    {
        this.userProvider = userProvider;
    }

    public void init(@NonNull final TabPane tabPane, @NonNull final ChatClient chatClient) {
        this.chatClient = chatClient;

        chatCommandHandlers = new HashMap<>();
        chatChannelControllers = new HashMap<>();

        final ChatChannelBuilder chatChannelBuilder = new ChatChannelBuilder(this);
        final ChatTabBuilder chatTabBuilder = new ChatTabBuilder(chatChannelBuilder, this);
        chatTabManager = new ChatTabManager(this, tabPane, chatTabBuilder);
        chatTabManager.init();

        addChatCommandHandlers();

        withClient();
    }

    private void withClient() {
        chatClient.start(this);
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
        chatCommandHandlers.put(WhisperCommandHandler.COMMAND, new WhisperCommandHandler(this));
        chatCommandHandlers.put(LeaveCommandHandler.COMMAND, new LeaveCommandHandler(this));
        chatCommandHandlers.put(ChuckNorrisCommandHandler.COMMAND, new ChuckNorrisCommandHandler(this, new RestTemplate()));
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
            receiveMessage(GENERAL_CHANNEL_NAME, SYSTEM, message);
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

    public void terminate() {
        chatClient.terminate();
        logger.debug("Terminated " + this);
    }
}
