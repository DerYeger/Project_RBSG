package de.uniks.se19.team_g.project_rbsg.lobby.model;

import javax.validation.constraints.NotNull;

/**
 * @author Georg Siebert
 */

public class Player
{
    private static final String defaultImagePath = "/assets/icons/navigation/accountWhite.png";
    private String name;
    private String imagePath;

    public Player(String name, String imagePath)
    {
        setName(name);
        if ("".equals(imagePath))
        {
            setImagePath(defaultImagePath);
        }
        else
        {
            setImagePath(imagePath);
        }
    }

    public Player(String name)
    {
        setName(name);
        setImagePath(defaultImagePath);
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getImagePath()
    {
        return this.imagePath;
    }

    public void setImagePath(@NotNull String imagePath)
    {
        this.imagePath = imagePath;
    }

}
