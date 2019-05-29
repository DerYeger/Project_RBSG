package de.uniks.se19.team_g.project_rbsg.Lobby.CrossCutting.DataClasses;

import javax.validation.constraints.NotNull;

/**
 * @author Georg Siebert
 */

public class Player
{
    private static final String defaultImagePath = "baseline_account_box_white_48dp.png";
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
