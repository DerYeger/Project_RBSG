package de.uniks.se19.team_g.project_rbsg.ingame.state;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GameObjectChangedAdapter implements GameEventDispatcher.Adapter {

    @Override
    public Optional<GameEvent> apply(ObjectNode jsonNodes, GameEventDispatcher dispatcher) {

        if (
                jsonNodes.has("action")
                && GameChangeObjectEvent.NAME.equals(jsonNodes.get("action").textValue())
                && jsonNodes.has("data")
                && jsonNodes.get("data").get("newValue").isValueNode()
        ) {
            return Optional.of(buildChangeEvent(jsonNodes.get("data"), dispatcher));
        }

        return Optional.empty();
    }

    private GameChangeObjectEvent buildChangeEvent(JsonNode data, GameEventDispatcher dispatcher) {
        return new GameChangeObjectEvent(
            data.get("id").textValue(),
            data.get("fieldName").textValue(),
            data.get("newValue").asText()
        );
    }
}
