package de.uniks.se19.team_g.project_rbsg.chat.command;

import de.uniks.se19.team_g.project_rbsg.chat.ChatClient;
import de.uniks.se19.team_g.project_rbsg.chat.ChatController;
import de.uniks.se19.team_g.project_rbsg.chat.ui.ChatChannelController;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * @author Jan Müller
 */
public class WhisperCommandHandler implements ChatCommandHandler {

    public static final String COMMAND = "w";

    public static final String OPTION_ERROR_MESSAGE = "/w <\"username\"> <message> - Send a private message to username";

    public static final String USER_ERROR_MESSAGE = "You can not chat with yourself!";

    private static final String pattern = "\"(.+\\s?)+\"\\s.+";

    private final ChatController chatController;

    public WhisperCommandHandler(@NonNull final ChatController chatController) {
        this.chatController = chatController;
    }

    @Override
    public String getCommand() {
        return COMMAND;
    }

    @Override
    public void handleCommand(@NonNull final ChatChannelController callback, @Nullable final String options) {
        if (options == null || options.isBlank() || !options.trim().matches(pattern)) {
            callback.displayMessage(ChatClient.SYSTEM, OPTION_ERROR_MESSAGE);
            return;
        }

        final String[] optionsArray = parseOptions(options);

        if (optionsArray[0].substring(1).equals(chatController.getUserName())) {
            callback.displayMessage(ChatClient.SYSTEM, USER_ERROR_MESSAGE);
            return;
        }

        chatController.sendMessage(callback, optionsArray[0], optionsArray[1]);
    }

    @NonNull
    private String[] parseOptions(@NonNull final String options) {
        final String[] optionsArray = new String[2];

        final int nameStartIndex = options.indexOf('"');

        final int nameEndIndex = options.indexOf('"', nameStartIndex + 1);

        optionsArray[0] = '@' + options.substring(nameStartIndex + 1, nameEndIndex);
        optionsArray[1] = options.substring(nameEndIndex + 2);

        return optionsArray;
    }
}
