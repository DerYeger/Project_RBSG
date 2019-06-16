package de.uniks.se19.team_g.project_rbsg.waiting_room.event;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

/**
 * @author Jan Müller
 */
public class DefaultGameEventHandler implements GameEventHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void handle(final @NonNull ObjectNode message)
    {
        if (message.has("action") && message.get("action").asText().equals("gameInitObject")) return;

        logger.debug(message.toString());
    }
}