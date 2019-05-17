package de.uniks.se19.team_g.project_rbsg.Lobby.Logic;

import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.Contract.DataClasses.User;
import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.Contract.ISystemMessageHandler;
import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.Contract.IWSCallback;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;


/**
 * @author Georg Siebert
 */

public class SystemMessageManager implements IWSCallback
{
    private static final String endpoint = "/system";

    private ArrayList<ISystemMessageHandler> systemMessageHandlers = new ArrayList<>();

    public SystemMessageManager(final @NotNull User user) {
        if(user != null) {
            WebSocketConfigurator.userKey = user.getUserKey();
            WebSocketClient webSocketClient = new WebSocketClient(endpoint, this);
        }
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
