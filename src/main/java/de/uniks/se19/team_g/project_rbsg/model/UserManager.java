package de.uniks.se19.team_g.project_rbsg.model;

import de.uniks.se19.team_g.project_rbsg.server.rest.LoginManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.LogoutManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.RegistrationManager;
import de.uniks.se19.team_g.project_rbsg.termination.Terminable;
import de.uniks.se19.team_g.project_rbsg.termination.Terminator;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * @author Jan Müller
 */
@Component
public class UserManager implements Terminable {

    @NonNull
    private final UserProvider userProvider;

    @NonNull
    private final RegistrationManager registrationManager;

    @NonNull
    private final LoginManager loginManager;

    @NonNull
    private final LogoutManager logoutManager;

    public UserManager(@NonNull final UserProvider userProvider,
                       @NonNull final RegistrationManager registrationManager,
                       @NonNull final LoginManager loginManager,
                       @NonNull final LogoutManager logoutManager,
                       @NonNull final Terminator terminator) {
        this.userProvider = userProvider;
        this.registrationManager = registrationManager;
        this.loginManager = loginManager;
        this.logoutManager = logoutManager;

        terminator.register(this);
    }

    @NonNull
    public UserProvider getUserProvider() {
        return userProvider;
    }

    @Override
    public void terminate() {
        logoutManager.logout(userProvider);
    }
}
