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
    public boolean accepts(final @NonNull ObjectNode message) {
        if (!message.has("action")) return true;

        return !message.get("action").asText().equals("gameInitObject")
                && !message.get("action").asText().equals("gameNewObject")
                && !message.get("action").asText().equals("gameRemoveObject");
    }

    @Override
    public void handle(final @NonNull ObjectNode message)
    {
         logger.debug(message.toString());
    }
}
