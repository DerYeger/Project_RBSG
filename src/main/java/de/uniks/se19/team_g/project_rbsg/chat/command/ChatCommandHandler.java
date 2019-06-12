package de.uniks.se19.team_g.project_rbsg.chat.command;

import de.uniks.se19.team_g.project_rbsg.chat.ChatChannelController;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * @author Jan Müller
 */
public interface ChatCommandHandler {
    String getCommand();
    void handleCommand(@NonNull final ChatChannelController callback, @Nullable final String options);
}
