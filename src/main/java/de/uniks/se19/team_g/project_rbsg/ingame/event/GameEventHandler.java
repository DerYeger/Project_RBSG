package de.uniks.se19.team_g.project_rbsg.ingame.event;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.lang.NonNull;

/**
 * @author Jan Müller
 */
public interface GameEventHandler {

    boolean handle(@NonNull final ObjectNode message);
}
