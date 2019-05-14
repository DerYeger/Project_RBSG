package de.uniks.se19.team_g.project_rbsg.model;

import org.springframework.lang.NonNull;

public class User {

    private String name;
    private String password;

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
}
