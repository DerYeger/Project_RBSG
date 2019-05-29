package de.uniks.se19.team_g.project_rbsg.model;

import org.springframework.lang.NonNull;

/**
 * @author Keanu Stückrad
 * @author Jan Müller
 */
public class User {

    private String name;
    private String password;
    private String userKey;

    public User() {
        //required for user provider
    }

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

    public String getName(){
        return this.name;
    }

    public void setUserKey(@NonNull final String userKey){
        this.userKey = userKey;
    }

<<<<<<< HEAD
=======
    public String getPassword() {
        return password;
    }

>>>>>>> createGame
    public User setName(@NonNull final String name) {
        this.name = name;
        return this;
    }

    public User setUserKey(@NonNull final String userKey) {
        this.userKey = userKey;
        return this;
    }

<<<<<<< HEAD
    public String getPassword() {
        return password;
    }
=======
>>>>>>> createGame
}
