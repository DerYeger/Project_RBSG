package de.uniks.se19.team_g.project_rbsg.model;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
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