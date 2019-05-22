package de.uniks.se19.team_g.project_rbsg.Lobby.Logic.SystemMessageHandler;

import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.Contract.ISystemMessageHandler;
import org.springframework.stereotype.Component;

@Component
public class DefaultSystemMessageHandler implements ISystemMessageHandler
{
    @Override
    public boolean handleSystemMessage(final String message)
    {
        System.out.println(message);
        return false;
    }
}
