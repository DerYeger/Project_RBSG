package de.uniks.se19.team_g.project_rbsg.handler;

import de.uniks.se19.team_g.project_rbsg.controller.ChatController;
import de.uniks.se19.team_g.project_rbsg.controller.ChatTabContentController;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * @author Jan Müller
 */
public class WhisperCommandHandler implements ChatCommandHandler {

    public static final String COMMAND = "w";

    public static final String OPTION_ERROR_MESSAGE = "Incorrect option pattern. /w \"username\" message";

    private static final String pattern = "\"(.+\\s?)+\"\\s.+";

    private ChatController chatController;

    public WhisperCommandHandler(@NonNull final ChatController chatController) {
        this.chatController = chatController;
    }

    //TODO check if user exists?
    @Override
    public void handleCommand(@NonNull final ChatTabContentController callback, @Nullable final String options) throws Exception {
        if (options == null || options.isBlank() || !options.trim().matches(pattern)) {
            callback.displayMessage(ChatController.SYSTEM, OPTION_ERROR_MESSAGE);
            return;
        }

        final String[] optionsArray = parseOptions(options.trim());

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
