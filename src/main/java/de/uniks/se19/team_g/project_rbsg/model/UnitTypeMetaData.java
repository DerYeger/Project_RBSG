package de.uniks.se19.team_g.project_rbsg.model;

import java.net.URL;

/**
 * TODO: Maybe use application.properties instead?
 */
public enum UnitTypeMetaData {

    UNKNOWN(
            UnitTypeMetaData.class.getResource("/assets/sprites/mr-unknown.png"),
            UnitTypeMetaData.class.getResource("/assets/icons/army/unknown-type.png")
    ),
    Infantry(
            UnitTypeMetaData.class.getResource("/assets/sprites/soldier.png"),
            UnitTypeMetaData.class.getResource("/assets/icons/army/crossed-swords white.png")
    ),
    BazookaTrooper(
            UnitTypeMetaData.class.getResource("/assets/sprites/soldier.png"),
            UnitTypeMetaData.class.getResource("/assets/icons/army/crossbow-white.png")
    ),
    ;

    private final URL image;
    private final URL icon;

    UnitTypeMetaData(
            URL image,
            URL icon
    ) {
        this.image = image;
        this.icon = icon;
    }

    public URL getImage() {
        return image;
    }

    public URL getIcon() {
        return icon;
    }
}
