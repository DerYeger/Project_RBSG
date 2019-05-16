package de.uniks.se19.team_g.project_rbsg.model;

import org.springframework.lang.NonNull;

/**
 * @author Juri Lozowoj
 */

public class User {

    private String name;
    private String password;
    private String userKey;

    public User(@NonNull String name, @NonNull String password){
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }
}
