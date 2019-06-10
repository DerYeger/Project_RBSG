package de.uniks.se19.team_g.project_rbsg.waiting_room.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

/**
 * @author Jan Müller
 */
public class DefaultGameEventHandler implements GameEventHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void handle(final @NonNull String message)
    {
        logger.debug(message);
    }
}
