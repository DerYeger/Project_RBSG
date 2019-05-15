package de.uniks.se19.team_g.project_rbsg.FeatureLobby.Logic.Contract.DataClasses;

/**
 * @author Georg Siebert
 */
public class User
{
    private String username;
    private String password;
    private String userKey;

    public User(String username, String password, String userKey)
    {
        this.username = username;
        this.password = password;
        this.userKey = userKey;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    public String getUserKey()
    {
        return userKey;
    }

    public void setUserKey(String userKey)
    {
        this.userKey = userKey;
    }
}
