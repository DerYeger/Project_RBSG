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

    // Constructor for UserClone
    public User(@NonNull final User user, @NonNull final String userKey){
        this.name = user.getName();
        this.userKey = userKey;
    }

    public String getUserKey() {
        return userKey;
    }

    public String getPassword() {
        return password;
    }
}
