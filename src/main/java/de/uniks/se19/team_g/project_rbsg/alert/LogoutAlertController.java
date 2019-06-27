package de.uniks.se19.team_g.project_rbsg.alert;

import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.server.rest.LogoutManager;
import org.springframework.lang.NonNull;

public class LogoutAlertController extends ConfirmationAlertController {

    @NonNull
    private final UserProvider userProvider;
    @NonNull
    private final LogoutManager logoutManager;

    public LogoutAlertController(@NonNull final UserProvider userProvider, @NonNull final LogoutManager logoutManager) {
        this.userProvider = userProvider;
        this.logoutManager = logoutManager;
    }

    //TODO replace placeholder
    @Override
    protected String getText() {
        return "Are you sure that you want to logout?";
    }

    @Override
    protected void onConfirm() {
        logoutManager.logout(userProvider);
    }
}
