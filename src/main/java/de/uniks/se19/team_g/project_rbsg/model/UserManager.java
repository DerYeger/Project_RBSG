package de.uniks.se19.team_g.project_rbsg.model;

import de.uniks.se19.team_g.project_rbsg.server.rest.LogoutManager;
import de.uniks.se19.team_g.project_rbsg.termination.Terminable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * @author Jan Müller
 */
@Component
public class UserManager implements Terminable {

    private final UserProvider userProvider;
    private final LogoutManager logoutManager;

    public UserManager(@NonNull final UserProvider userProvider,
                       @NonNull final LogoutManager logoutManager) {
        this.userProvider = userProvider;
        this.logoutManager = logoutManager;
    }

    @NonNull
    public UserProvider getUserProvider() {
        return userProvider;
    }

    @Override
    public void terminate() {
        logoutManager.logout(userProvider);
        userProvider.clear();
    }
}
