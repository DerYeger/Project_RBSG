package de.uniks.se19.team_g.project_rbsg.model;

import org.springframework.stereotype.Component;

@Component
public class UserProvider {

    private User user;

    public User getUser() {
        if (user == null) {
            user = new User();
        }
        return user;
    }

    public UserProvider clear() {
        user = null;
        return this;
    }
}
