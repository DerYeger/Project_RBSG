package de.uniks.se19.team_g.project_rbsg.lobby.chat;

import de.uniks.se19.team_g.project_rbsg.server.websocket.IWebSocketCallback;
import de.uniks.se19.team_g.project_rbsg.termination.Terminable;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public interface ChatClient extends IWebSocketCallback, Terminable {

    String CLIENT_PUBLIC_CHANNEL = "General";

    String SERVER_PUBLIC_CHANNEL = "all";
    String SERVER_PRIVATE_CHANNEL = "private";

    void start(@NonNull final ChatController chatController);
    void sendMessage(@NonNull final String channel, @Nullable final String to, @NonNull final String message);
}
