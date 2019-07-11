package de.uniks.se19.team_g.project_rbsg.ingame.waiting_room.event;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.lang.NonNull;

/**
 * @author Jan Müller
 */
public interface GameEventHandler {

    default boolean accepts(@NonNull final ObjectNode message) {
        return true;
    }

    void handle(@NonNull final ObjectNode message);
}
