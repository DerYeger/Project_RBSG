package de.uniks.se19.team_g.project_rbsg.handler;

import de.uniks.se19.team_g.project_rbsg.controller.ChatController;
import de.uniks.se19.team_g.project_rbsg.controller.ChatTabContentController;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * @author Jan Müller
 */
public class WhisperCommandHandler implements ChatCommandHandler {

    private static final String COMMAND = "w";

    public static final String OPTION_ERROR_MESSAGE = "Incorrect option pattern. Use /w userName";

    public static final String CHANNEL_ERROR_MESSAGE = "You already joined that channel";

    private ChatController chatController;

    public WhisperCommandHandler(@NonNull final ChatController chatController) {
        this.chatController = chatController;
    }

    //TODO check if user exists?
    @Override
    public boolean handleCommand(@NonNull final ChatTabContentController callback, @NonNull final String command, @Nullable final String[] options) throws Exception {
        if (command.equals(COMMAND)) { //matching command
            if (options == null || options.length < 1) { //option pattern error
                callback.displayMessage(chatController.SYSTEM, OPTION_ERROR_MESSAGE);
            } else if (!chatController.addPrivateTab(options[0])) { //channel error
                callback.displayMessage(chatController.SYSTEM, CHANNEL_ERROR_MESSAGE);
            }
            return true;
        }
        return false;
    }
}
