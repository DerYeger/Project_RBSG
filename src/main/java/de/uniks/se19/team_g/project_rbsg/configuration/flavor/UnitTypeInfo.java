package de.uniks.se19.team_g.project_rbsg.configuration.flavor;

import javafx.beans.property.*;
import javafx.scene.image.Image;

import javax.annotation.Nonnull;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Maybe use application.properties instead?
 */
public enum UnitTypeInfo {

    UNKNOWN(
            null,
            "flavor.unit.unknown.description",
            UnitTypeInfo.class.getResource("/assets/sprites/mr-unknown.png"),
            UnitTypeInfo.class.getResource("/assets/icons/army/unknown-type.png")
    ),
    _INFANTRY(
            "flavor.unit.infantry.name",
            "flavor.unit.infantry.description",
            UnitTypeInfo.class.getResource("/assets/sprites/soldier.gif"),
            UnitTypeInfo.class.getResource("/assets/unit/icon/black-knight-helm.png")
    ),
    _BAZOOKA_TROOPER(
            "flavor.unit.bazookaTrooper.name",
            "flavor.unit.bazookaTrooper.description",
            UnitTypeInfo.class.getResource("/assets/unit/portrait/khorneberzerker.png"),
            UnitTypeInfo.class.getResource("/assets/unit/icon/overlord-helm.png")
    ),
    _JEEP(
            "flavor.unit.jeep.name",
            "flavor.unit.jeep.description",
            UnitTypeInfo.class.getResource("/assets/sprites/mr-unknown.png"),
            UnitTypeInfo.class.getResource("/assets/unit/icon/cultist.png")
    ),
    _LIGHT_TANK(
            "flavor.unit.lightTank.name",
            "flavor.unit.lightTank.description",
            UnitTypeInfo.class.getResource("/assets/unit/portrait/skeleton.png"),
            UnitTypeInfo.class.getResource("/assets/unit/icon/sword-wound.png")
    ),
    _HEAVY_TANK(
            "flavor.unit.heavyTank.name",
            "flavor.unit.heavyTank.description",
            UnitTypeInfo.class.getResource("/assets/unit/portrait/chubby-transparent.gif"),
            UnitTypeInfo.class.getResource("/assets/unit/icon/dwarf-face.png")
    ),
    _CHOPPER(
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
        return UnitImageResolver.getUnitImage(this);
    }

    public Image getPreview(int height, int width) {
        return UnitImageResolver.getUnitImage(this, height, width);
    }

    public ObjectProperty<Image> getImageProperty() {
        return UnitImageResolver.getUnitImageProperty(this);
    }

    public Image getIconImage() {
        return new Image(image.toExternalForm());
    }

    public URL getImage() {
        return UnitImageResolver.getUnitImageURL(this);
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

    @Nonnull
    public static UnitTypeInfo resolveType(String type) {
        String identifier = "_" + type.toUpperCase().trim().replaceAll("\\s+", "_");

        try {
            return UnitTypeInfo.valueOf(identifier);
        } catch (IllegalArgumentException e) {
            return UnitTypeInfo.UNKNOWN;
        }
    }
}
