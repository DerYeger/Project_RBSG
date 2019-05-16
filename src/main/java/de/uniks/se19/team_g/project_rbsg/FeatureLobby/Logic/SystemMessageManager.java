package de.uniks.se19.team_g.project_rbsg.FeatureLobby.Logic;

import de.uniks.se19.team_g.project_rbsg.FeatureLobby.Logic.Contract.ISystemMessageHandler;
import de.uniks.se19.team_g.project_rbsg.FeatureLobby.Logic.Contract.IWSCallback;

import java.util.ArrayList;

public class SystemMessageManager implements IWSCallback
{
    private ArrayList<ISystemMessageHandler> systemMessageHandlers = new ArrayList<>();

    public SystemMessageManager() {

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
