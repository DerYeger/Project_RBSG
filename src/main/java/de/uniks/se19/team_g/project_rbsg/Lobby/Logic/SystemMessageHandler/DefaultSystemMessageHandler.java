package de.uniks.se19.team_g.project_rbsg.Lobby.Logic.SystemMessageHandler;

import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.Contract.ISystemMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;


@Component
public class DefaultSystemMessageHandler implements ISystemMessageHandler
{
    private final Logger logger = LoggerFactory.getLogger(Logger.class);

    @Override
    public void handleSystemMessage(final @NonNull String message)
    {
        logger.debug(message);
    }
}
