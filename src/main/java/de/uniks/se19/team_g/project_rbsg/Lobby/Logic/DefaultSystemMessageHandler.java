package de.uniks.se19.team_g.project_rbsg.Lobby.Logic;

import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.Contract.ISystemMessageHandler;

public class DefaultSystemMessageHandler implements ISystemMessageHandler
{
    @Override
    public boolean handleSystemMessage(final String message)
    {
        System.out.println(message);
        return false;
    }
}
