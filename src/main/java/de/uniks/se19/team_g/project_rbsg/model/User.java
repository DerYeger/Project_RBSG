package de.uniks.se19.team_g.project_rbsg.model;

import org.springframework.lang.NonNull;

/**
 * @author Keanu Stückrad
 */
public class User {

    private String name;
    private String password;
    private String userKey;

    public User(@NonNull final String name, @NonNull final String password){
        this.name = name;
        this.password = password;
    }

    public String getUserKey() {
        return userKey;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public void setUserKey(final String userKey) {
        this.userKey = userKey;
    }
}
