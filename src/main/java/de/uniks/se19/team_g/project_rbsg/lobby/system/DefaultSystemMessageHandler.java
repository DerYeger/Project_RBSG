package de.uniks.se19.team_g.project_rbsg.lobby.core.SystemMessageHandler;

import de.uniks.se19.team_g.project_rbsg.lobby.system.ISystemMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;


/**
 * @author Georg Siebert
 */
public class DefaultSystemMessageHandler implements ISystemMessageHandler
{
    private final Logger logger = LoggerFactory.getLogger(DefaultSystemMessageHandler.class);

    @Override
    public void handleSystemMessage(final @NonNull String message)
    {
        logger.debug(message);
    }
}
