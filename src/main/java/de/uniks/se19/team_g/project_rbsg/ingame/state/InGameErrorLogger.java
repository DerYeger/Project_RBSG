package de.uniks.se19.team_g.project_rbsg.ingame.state;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class InGameErrorLogger implements GameEventDispatcher.Adapter {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Optional<GameEvent> apply(ObjectNode jsonNodes, GameEventDispatcher dispatcher) {

        JsonNode action = jsonNodes.get("action");
        if (action != null && "inGameError".equals(action.asText())) {
            logger.error(jsonNodes.get("data").asText());
        }

        return Optional.empty();
    }

    @Override
    public int getPriority() {
        return 100;
    }
}
