package de.uniks.se19.team_g.project_rbsg.Lobby.Logic;

import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.Contract.IWebSocketCallback;
import org.springframework.stereotype.Component;

@Component
public class WebSocketFactory
{
    public WebSocketClient getSocket(String endpoint, IWebSocketCallback webSocketCallback) {
        return new WebSocketClient(endpoint, webSocketCallback);
    }
}
