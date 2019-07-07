package de.uniks.se19.team_g.project_rbsg.configuration.flavor;

import javafx.scene.image.Image;

import java.net.URL;

/**
 * TODO: Maybe use application.properties instead?
 */
public enum UnitTypeInfo {

    UNKNOWN(
            "flavor.unit.unknown.name",
            "flavor.unit.unknown.description",
            UnitTypeInfo.class.getResource("/assets/sprites/mr-unknown.png"),
            UnitTypeInfo.class.getResource("/assets/icons/army/unknown-type.png")
    ),
    _5cc051bd62083600017db3b6(
            "flavor.unit.infantry.name",
            "flavor.unit.infantry.description",
            UnitTypeInfo.class.getResource("/assets/sprites/soldier.gif"),
            UnitTypeInfo.class.getResource("/assets/unit/icon/black-knight-helm.png")
    ),
    _5cc051bd62083600017db3b7(
            "flavor.unit.bazookaTrooper.name",
            "flavor.unit.bazookaTrooper.description",
            UnitTypeInfo.class.getResource("/assets/unit/portrait/khorneberzerker.png"),
            UnitTypeInfo.class.getResource("/assets/unit/icon/overlord-helm.png")
    ),
    _5cc051bd62083600017db3b8(
            "flavor.unit.jeep.name",
            "flavor.unit.jeep.description",
            UnitTypeInfo.class.getResource("/assets/sprites/mr-unknown.png"),
            UnitTypeInfo.class.getResource("/assets/unit/icon/cultist.png")
    ),
    _5cc051bd62083600017db3b9(
            "flavor.unit.lightTank.name",
            "flavor.unit.lightTank.description",
            UnitTypeInfo.class.getResource("/assets/unit/portrait/skeleton.png"),
            UnitTypeInfo.class.getResource("/assets/unit/icon/sword-wound.png")
    ),
    _5cc051bd62083600017db3ba(
            "flavor.unit.heavyTank.name",
            "flavor.unit.heavyTank.description",
            UnitTypeInfo.class.getResource("/assets/unit/portrait/chubby-transparent.gif"),
            UnitTypeInfo.class.getResource("/assets/unit/icon/dwarf-face.png")
    ),
    _5cc051bd62083600017db3bb(
            "flavor.unit.chopper.name",
            "flavor.unit.chopper.description",
            UnitTypeInfo.class.getResource("/assets/unit/portrait/bird.gif"),
            UnitTypeInfo.class.getResource("/assets/unit/icon/dragon-head.png")
    ),
    ;

    private final String nameKey;
    private final String descriptionKey;
    private final URL image;
    private final URL icon;

    UnitTypeInfo(
        String nameKey,
        String descriptionKey,
        URL image,
        URL icon
    ) {
        this.nameKey = nameKey;
        this.descriptionKey = descriptionKey;
        this.image = image;
        this.icon = icon;
    }

    public Image getPreview() {
        return new Image(image.toExternalForm());
    }

    public Image getIconImage() {
        return new Image(image.toExternalForm());
    }

    public URL getImage() {
        return image;
    }

    public URL getIcon() {
        return icon;
    }

    public String getNameKey() {
        return nameKey;
    }

    public String getDescriptionKey() {
        return descriptionKey;
    }
}
