package de.uniks.se19.team_g.project_rbsg.Lobby.Logic;

import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.Contract.ISystemMessageHandler;
import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.Contract.IWebSocketCallback;
import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.SystemMessageHandler.DefaultSystemMessageHandler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

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

    public void setWebSocketClient(WebSocketClient client) {
        this.webSocketClient = client;
    }

    public WebSocketClient getWebSocketClient() {
        return this.webSocketClient;
    }

    public SystemMessageManager(WebSocketClient webSocketClient) {
        systemMessageHandlers = new ArrayList<>();
        systemMessageHandlers.add(new DefaultSystemMessageHandler());

        this.webSocketClient = webSocketClient;
    }

    public void startSocket() {
        if (WebSocketConfigurator.userKey.equals("") || webSocketClient == null) {
            return;
        }
        webSocketClient.start("/system", this);
    }

    public void addMessageHandler(final @NonNull ISystemMessageHandler messageHandler) {
        systemMessageHandlers.add(messageHandler);
    }

    @Override
    public void handle(final String message)
    {
        for (ISystemMessageHandler systemMessageHandler : systemMessageHandlers)
        {
           systemMessageHandler.handleSystemMessage(message);
        }
    }
}
