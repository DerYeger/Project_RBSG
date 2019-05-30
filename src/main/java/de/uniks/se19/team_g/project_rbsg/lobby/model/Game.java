package de.uniks.se19.team_g.project_rbsg.lobby.model;

/**
 * @author Georg Siebert
 */
public class Game
{
    private String id;
    private String name;
    private String imagePath;

    private int neededPlayer;
    private int joinedPlayer;

    public Game(String id, String name, String imagePath, int neededPlayer, int joinedPlayer)
    {
        this.id = id;
        this.name = name;
        this.imagePath = imagePath;
        this.neededPlayer = neededPlayer;
        this.joinedPlayer = joinedPlayer;
    }

    public Game(String id, String name, int neededPlayer, int joinedPlayer)
    {
        this.id = id;
        this.name = name;
        this.neededPlayer = neededPlayer;
        this.joinedPlayer = joinedPlayer;

        //TODO: SetDefaultImagePath
        this.imagePath = "";
    }


    public String getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getImagePath()
    {
        return imagePath;
    }

    public int getNeededPlayer()
    {
        return neededPlayer;
    }

    public int getJoinedPlayer()
    {
        return joinedPlayer;
    }

    public void setJoinedPlayer(int joinedPlayer) {
        this.joinedPlayer = joinedPlayer;
    }
}
