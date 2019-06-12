package de.uniks.se19.team_g.project_rbsg.chat.command;

import de.uniks.se19.team_g.project_rbsg.chat.ChatChannelController;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@Scope("prototype")
public class ChatCommandManager {

    private HashMap<String, ChatCommandHandler> chatCommandHandlers;

    public ChatCommandManager() {
        chatCommandHandlers = new HashMap<>();
    }

    public ChatCommandManager addHandler(@NonNull final ChatCommandHandler chatCommandHandler) {
        chatCommandHandlers.put(chatCommandHandler.getCommand(), chatCommandHandler);
        return this;
    }

    public HashMap<String, ChatCommandHandler> getHandlers() {
        return chatCommandHandlers;
    }

    public boolean handleCommand(@NonNull final ChatChannelController callback, @NonNull final String command, @Nullable final String options) {
        if (chatCommandHandlers.containsKey(command)) {
            chatCommandHandlers.get(command).handleCommand(callback, options);
            return true;
        }
        return false;
    }

}
