package de.uniks.se19.team_g.project_rbsg.FeatureLobby.Logic;

import de.uniks.se19.team_g.project_rbsg.FeatureLobby.Logic.Contract.ISystemMessageHandler;

public class DefaultSystemMessageHandler implements ISystemMessageHandler
{
    @Override
    public boolean handleSystemMessage(String message)
    {
        System.out.println(message);
        return false;
    }
}
