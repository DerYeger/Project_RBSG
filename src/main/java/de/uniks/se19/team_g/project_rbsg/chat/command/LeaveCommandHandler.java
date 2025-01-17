package de.uniks.se19.team_g.project_rbsg.chat.command;

import de.uniks.se19.team_g.project_rbsg.chat.ui.ChatChannelController;
import de.uniks.se19.team_g.project_rbsg.chat.ChatController;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import static de.uniks.se19.team_g.project_rbsg.chat.ChatClient.SYSTEM;

/**
 * @author Jan Müller
 */
public class LeaveCommandHandler implements ChatCommandHandler {

    public static final String COMMAND = "leave";

    public static final String REMOVE_ERROR = "Unable to leave the specified channel";

    private final ChatController chatController;

    public LeaveCommandHandler(@NonNull final ChatController chatController) {
        this.chatController = chatController;
    }

    @Override
    public String getCommand() {
        return COMMAND;
    }

    @Override
    public void handleCommand(@NonNull final ChatChannelController callback, @Nullable final String options) {
        String channelToRemove = callback.getChannel();
        if (options != null && !options.isBlank()) {
            channelToRemove = '@' + options.trim();
        }

        if (!chatController.closeChannel(channelToRemove)) {
            chatController.receiveMessage(callback.getChannel(), SYSTEM, REMOVE_ERROR);
        }
    }
}
