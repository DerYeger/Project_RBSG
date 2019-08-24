package de.uniks.se19.team_g.project_rbsg.configuration.flavor;

import javafx.scene.image.Image;

import javax.annotation.Nonnull;
import java.net.URL;
import java.util.HashMap;

/**
 * TODO: Maybe use application.properties instead?
 */
public enum UnitTypeInfo {

    UNKNOWN(
            null,
            "flavor.unit.unknown.description",
            UnitTypeInfo.class.getResource("/assets/sprites/mr-unknown.png"),
            UnitTypeInfo.class.getResource("/assets/icons/army/unknown-type.png"),
            new CanAttack(0, 0, 0, 0, 0, 0)
    ),
    _INFANTRY(
            "flavor.unit.infantry.name",
            "flavor.unit.infantry.description",
            UnitTypeInfo.class.getResource("/assets/sprites/soldier.gif"),
            UnitTypeInfo.class.getResource("/assets/unit/icon/black-knight-helm.png"),
            new CanAttack(55, 45, 12, 5, 1, 0)
    ),
    _BAZOOKA_TROOPER(
            "flavor.unit.bazookaTrooper.name",
            "flavor.unit.bazookaTrooper.description",
            UnitTypeInfo.class.getResource("/assets/unit/portrait/khorneberzerker.png"),
            UnitTypeInfo.class.getResource("/assets/unit/icon/overlord-helm.png"),
            new CanAttack(0, 0, 85, 55, 15, 55)
    ),
    _JEEP(
            "flavor.unit.jeep.name",
            "flavor.unit.jeep.description",
            UnitTypeInfo.class.getResource("/assets/sprites/mr-unknown.png"),
            UnitTypeInfo.class.getResource("/assets/unit/icon/cultist.png"),
            new CanAttack(70, 65, 35, 0, 0, 0)
    ),
    _LIGHT_TANK(
            "flavor.unit.lightTank.name",
            "flavor.unit.lightTank.description",
            UnitTypeInfo.class.getResource("/assets/unit/portrait/skeleton.png"),
            UnitTypeInfo.class.getResource("/assets/unit/icon/sword-wound.png"),
            new CanAttack(75, 70, 85, 55, 15, 0)
    ),
    _HEAVY_TANK(
            "flavor.unit.heavyTank.name",
            "flavor.unit.heavyTank.description",
            UnitTypeInfo.class.getResource("/assets/unit/portrait/chubby-transparent.gif"),
            UnitTypeInfo.class.getResource("/assets/unit/icon/dwarf-face.png"),
            new CanAttack(105, 95, 105, 85, 55, 75)
    ),
    _CHOPPER(
            "flavor.unit.chopper.name",
            "flavor.unit.chopper.description",
            UnitTypeInfo.class.getResource("/assets/unit/portrait/bird.gif"),
            UnitTypeInfo.class.getResource("/assets/unit/icon/dragon-head.png"),
            new CanAttack(75, 75, 55, 25, 20, 0)
    ),
    ;

    private final String nameKey;
    private final String descriptionKey;
    private final URL image;
    private final URL icon;
    private final CanAttack canAttack;

    UnitTypeInfo(
            String nameKey,
            String descriptionKey,
            URL image,
            URL icon,
            CanAttack canAttack
    ) {
        this.nameKey = nameKey;
        this.descriptionKey = descriptionKey;
        this.image = image;
        this.icon = icon;
        this.canAttack = canAttack;
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

    public int getCanAttack(UnitTypeInfo unitTypeInfo) {
        return canAttack.getAttackValue(unitTypeInfo);
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

    /**
     * Who doesn't love hardcoding?
     */
    private static class CanAttack {

        private final HashMap<UnitTypeInfo, Integer> attackValue = new HashMap<>();

        CanAttack(int attackValueInfantry,
                  int attackValueBazooka,
                  int attackValueJeep,
                  int attackValueLight,
                  int attackValueHeavy,
                  int attackValueChopper
        ) {
            attackValue.put(_INFANTRY, attackValueInfantry);
            attackValue.put(_BAZOOKA_TROOPER, attackValueBazooka);
            attackValue.put(_JEEP, attackValueJeep);
            attackValue.put(_LIGHT_TANK, attackValueLight);
            attackValue.put(_HEAVY_TANK, attackValueHeavy);
            attackValue.put(_CHOPPER, attackValueChopper);
        }

        private int getAttackValue(UnitTypeInfo unitTypeInfo) {
            return attackValue.get(unitTypeInfo);
        }

    }
}
