package de.uniks.se19.team_g.project_rbsg.FeatureLobby.UI.ViewModels.Contract.DataClasses;

import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

/**
 * @author Georg Siebert
 */

public class Player
{
    private @NotNull String name;

    private @Nullable String imagePath;

    private static final String defaultImagePath = "baseline_account_circle_black_48dp.png";

    public String getName() {
        return this.name;
    }

    public void setName(@NotNull String  name) {
        this.name = name;
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public void setImagePath(@Nullable String imagePath) {
        this.imagePath = imagePath;
    }

    public Player(String name, String imagePath) {
        setName(name);
        setImagePath(imagePath);
    }



    public Player(String name) {
        setName(name);
        setImagePath(defaultImagePath);
    }

}
