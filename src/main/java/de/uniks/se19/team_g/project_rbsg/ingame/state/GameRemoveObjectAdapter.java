package de.uniks.se19.team_g.project_rbsg.ingame.state;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Cell;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Game;
import de.uniks.se19.team_g.project_rbsg.ingame.model.ModelManager;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import de.uniks.se19.team_g.project_rbsg.ingame.model.util.PlayerUtil;
import de.uniks.se19.team_g.project_rbsg.ingame.model.util.UnitUtil;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GameRemoveObjectAdapter implements GameEventDispatcher.Adapter {

    @Override
    public Optional<GameEvent> apply(ObjectNode jsonNodes, GameEventDispatcher dispatcher) {

        if ( !isValid(jsonNodes)) {
            return Optional.empty();
        }

        return Optional.of(buildEvent(jsonNodes.get("data")));
    }

    private GameRemoveObjectEvent buildEvent(JsonNode data) {
        String id = data.get("id").textValue();
        String type = ModelManager.splitIdentifier(id).first;

        String fieldName = getMappedFieldName(data.get("fieldName").textValue(), type);

        return new GameRemoveObjectEvent(
                id,
                data.get("from").textValue(),
                fieldName
        );
    }

    private boolean isValid(ObjectNode jsonNodes) {
        JsonNode data = jsonNodes.get("data");
        if (data == null || !data.isObject()) {
            return false;
        }

        return jsonNodes.has("action")
            && GameRemoveObjectEvent.NAME.equals(jsonNodes.get("action").textValue())
            && data.has("from")
            && data.has("fieldName")
            && data.has("id")
        ;
    }


    private String getMappedFieldName(String fieldName, String type) {
        if ("Unit".equals(type)) {
            switch (fieldName) {
                case UnitUtil.GAME_UNITS:
                    return Game.UNITS;
                case UnitUtil.PLAYER_UNITS:
                    return Player.UNITS;
                case UnitUtil.CELL:
                    return Cell.UNIT;
            }
        }
        if ("Player".equals(type)) {
            if (PlayerUtil.PLAYERS.equals(fieldName)) {
                return Game.PLAYERS;
            }
        }

        return fieldName;
    }
}
