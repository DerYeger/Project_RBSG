package de.uniks.se19.team_g.project_rbsg.waiting_room.event;

import org.springframework.lang.NonNull;

/**
 * @author Jan Müller
 */
public interface GameEventHandler {

    //TODO arguments
    void handle(@NonNull final String message);
}
