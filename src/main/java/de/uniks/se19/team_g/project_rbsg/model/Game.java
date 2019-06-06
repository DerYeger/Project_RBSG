package de.uniks.se19.team_g.project_rbsg.model;

import org.springframework.lang.NonNull;

/**
 * @author Georg Siebert
 * @edited Keanu Stückrad
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

    public Game(@NonNull String name, @NonNull int neededPlayer){
        this.name = name;
        this.neededPlayer = neededPlayer;
        this.joinedPlayer = 0;

        //TODO: SetDefaultImagePath
        this.imagePath = "";
    }


    public String getId()
    {
        return id;
    }

    public void setId(String id) {
        if(this.id == null){
            this.id = id;
        }
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
