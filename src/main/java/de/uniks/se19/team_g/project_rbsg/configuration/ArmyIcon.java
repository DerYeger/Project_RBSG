package de.uniks.se19.team_g.project_rbsg.configuration;

import javafx.scene.image.Image;

import java.net.URL;

public enum ArmyIcon {


    DRAGON_HEAD(
        ArmyIcon.class.getResource("/assets/icons/army/dragon-head.white.png")
    ),
    WOLF_HEAD(
        ArmyIcon.class.getResource("/assets/icons/army/wolf-head.white.png")
    )
    ;

    private final URL imageUrl;

    private Image image;


    ArmyIcon(URL imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Image getImage() {
        if (image == null) {
            this.image = new Image(imageUrl.toExternalForm());
        }
        return image;
    }
}
