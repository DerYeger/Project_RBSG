package de.uniks.se19.team_g.project_rbsg.ingame.event;

import org.springframework.lang.NonNull;

/**
 * @author Jan Müller
 */
public interface WaitingRoomEventHandler {

    //TODO arguments
    void handle(@NonNull final String message);
}
