package de.uniks.se19.team_g.project_rbsg;

public class User {

    private String name;

    private String password;

    private String userKey;

    public User(String name, String password){
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

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }
}
