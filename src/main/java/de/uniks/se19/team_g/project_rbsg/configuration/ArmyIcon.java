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

    private final Image image;

    ArmyIcon(URL resource) {
        this.image = new Image(resource.toExternalForm());
    }

    public Image getImage() {
        return image;
    }
}
