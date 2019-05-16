package de.uniks.se19.team_g.project_rbsg.util;

import de.uniks.se19.team_g.project_rbsg.controller.ChatController;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * @author Jan Müller
 */
public class WhisperCommandHandler implements ChatCommandHandler {

    private static final String COMMAND = "w";

    private ChatController chatController;

    public WhisperCommandHandler(@NonNull final ChatController chatController) {
        this.chatController = chatController;
    }


    //TODO check if user exists?
    @Override
    public boolean handleCommand(@NonNull final String command, @Nullable final String[] options) throws Exception {
        if (command.equals(COMMAND)) { //matching command
            if (options != null && options.length > 0) { //matching options
                chatController.addPrivateTab(options[0]);
                return true;
            }
        }
        return false;
    }
}
