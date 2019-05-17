package de.uniks.se19.team_g.project_rbsg.handler;

import de.uniks.se19.team_g.project_rbsg.controller.ChatChannelController;
import de.uniks.se19.team_g.project_rbsg.controller.ChatController;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public class LeaveCommandHandler implements ChatCommandHandler {

    public static final String COMMAND = "leave";

    public static final String REMOVE_ERROR = "Unable to leave the specified channel";

    private final ChatController chatController;

    public LeaveCommandHandler(@NonNull final ChatController chatController) {
        this.chatController = chatController;
    }

    @Override
    public void handleCommand(@NonNull final ChatChannelController callback, @Nullable final String options) throws Exception {
        String channelToRemove = callback.getChannel();
        if (options != null && !options.isBlank()) {
            channelToRemove = options;
        }

        if (!chatController.removeTab(channelToRemove)) {
            chatController.receiveMessage(callback.getChannel(), ChatController.SYSTEM, REMOVE_ERROR);
        }
    }
}
