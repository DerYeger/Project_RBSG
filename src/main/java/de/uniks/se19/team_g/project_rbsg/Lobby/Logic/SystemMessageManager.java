package de.uniks.se19.team_g.project_rbsg.Lobby.Logic;

import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.Contract.ISystemMessageHandler;
import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.Contract.IWebSocketCallback;
import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.SystemMessageHandler.DefaultSystemMessageHandler;
import de.uniks.se19.team_g.project_rbsg.model.User;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;


/**
 * @author Georg Siebert
 */

@Component
public class SystemMessageManager implements IWebSocketCallback
{
    private static final String endpoint = "/system";

    private ArrayList<ISystemMessageHandler> systemMessageHandlers;
    private WebSocketClient webSocketClient;

    public SystemMessageManager(final DefaultSystemMessageHandler defaultSystemMessageHandler, WebSocketClient webSocketClient) {
        systemMessageHandlers = new ArrayList<>();
        systemMessageHandlers.add(defaultSystemMessageHandler);

        this.webSocketClient = webSocketClient;
        webSocketClient.start(endpoint, this);
    }

    @Override
    public void handle(final String message)
    {
        for (ISystemMessageHandler systemMessageHandler : systemMessageHandlers)
        {
            if(systemMessageHandler.handleSystemMessage(message)) {
                break;
            }
        }
    }
}
