package de.uniks.se19.team_g.project_rbsg.server.rest;

import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import org.springframework.lang.NonNull;

public interface LogoutManager {
    void logout(@NonNull final UserProvider userProvider);
}
