package de.uniks.se19.team_g.project_rbsg.model;

import de.uniks.se19.team_g.project_rbsg.bots.UserScoped;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * @author Jan Müller
 */
@Component
@UserScoped
public class UserProvider {

    private User user;

    public User get() {
        if (user == null) {
            user = new User();
        }
        return user;
    }

    public UserProvider set(@NonNull final User user) {
        this.user = user;
        return this;
    }

    public UserProvider clear() {
        user = null;
        return this;
    }
}
