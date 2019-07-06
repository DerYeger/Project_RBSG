package de.uniks.se19.team_g.project_rbsg.configuration.flavor;

import javafx.scene.image.Image;

import java.net.URL;

public enum ArmyIcon {

    BLACK_KNIGHT(
        ArmyIcon.class.getResource("/assets/unit/icon/black-knight-helm.png")
    ),
    CULTIST(
            ArmyIcon.class.getResource("/assets/unit/icon/cultist.png")
    ),
    DRAGON_HEAD(
            ArmyIcon.class.getResource("/assets/unit/icon/dragon-head.png")
    ),
    DWARF_FACE(
            ArmyIcon.class.getResource("/assets/unit/icon/dwarf-face.png")
    ),
    OVERLORD_HELM(
            ArmyIcon.class.getResource("/assets/unit/icon/overlord-helm.png")
    ),
    RALLY_THE_TROOPS(
            ArmyIcon.class.getResource("/assets/unit/icon/rallyTroopsBlack.png")
    ),
    SWORD_WOUND(
            ArmyIcon.class.getResource("/assets/unit/icon/sword-wound.png")
    ),
    WOLF_HEAD(
            ArmyIcon.class.getResource("/assets/unit/icon/wolf-head.png")
    ),
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

    public static ArmyIcon resolveValue(String name) {
        try {
            return valueOf(name);
        } catch (IllegalArgumentException e) {
            return DRAGON_HEAD;
        }
    }
}
