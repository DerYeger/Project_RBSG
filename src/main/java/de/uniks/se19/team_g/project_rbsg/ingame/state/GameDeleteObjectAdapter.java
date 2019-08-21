package de.uniks.se19.team_g.project_rbsg.ingame.state;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GameDeleteObjectAdapter implements GameEventDispatcher.Adapter {
    @Override
    public Optional<GameEvent> apply(ObjectNode jsonNodes, GameEventDispatcher dispatcher) {

        if ( isValid(jsonNodes)) {
            return Optional.empty();
        }

        return Optional.of(buildEvent(jsonNodes.get("data")));
    }

    private GameDeleteObjectEvent buildEvent(JsonNode data) {
        return new GameDeleteObjectEvent(
            data.get("id").textValue(),
            data.get("fieldName").textValue(),
            data.get("from").textValue()
        );
    }

    private boolean isValid(ObjectNode jsonNodes) {
        JsonNode data = jsonNodes.get("data");
        if (data == null || !data.isObject()) {
            return false;
        }

        return jsonNodes.has("action")
            && GameDeleteObjectEvent.NAME.equals(jsonNodes.get("action").textValue())
            && data.has("from")
            && data.has("fieldName")
            && data.has("id")
        ;
    }
}
