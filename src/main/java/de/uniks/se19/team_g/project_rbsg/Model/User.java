package de.uniks.se19.team_g.project_rbsg.Model;

public class User {

    private String name;
    private String password;

    public User(String name, String password){
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
