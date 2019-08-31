package de.uniks.se19.team_g.project_rbsg.configuration.flavor;

import de.uniks.se19.team_g.project_rbsg.util.AttackCalculator;
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
            UnitTypeInfo.class.getResource("/assets/icons/army/unknown-type.png"),
            new CanAttack(AttackCalculator.getAttackValues(""))
    ),
    _INFANTRY(
            "flavor.unit.infantry.name",
            "flavor.unit.infantry.description",
            UnitTypeInfo.class.getResource("/assets/sprites/soldier.gif"),
            UnitTypeInfo.class.getResource("/assets/unit/icon/black-knight-helm.png"),
            new CanAttack(AttackCalculator.getAttackValues("Infantry"))
    ),
    _BAZOOKA_TROOPER(
            "flavor.unit.bazookaTrooper.name",
            "flavor.unit.bazookaTrooper.description",
            UnitTypeInfo.class.getResource("/assets/unit/portrait/khorneberzerker.png"),
            UnitTypeInfo.class.getResource("/assets/unit/icon/overlord-helm.png"),
            new CanAttack(AttackCalculator.getAttackValues("Bazooka"))
    ),
    _JEEP(
            "flavor.unit.jeep.name",
            "flavor.unit.jeep.description",
            UnitTypeInfo.class.getResource("/assets/sprites/mr-unknown.png"),
            UnitTypeInfo.class.getResource("/assets/unit/icon/cultist.png"),
            new CanAttack(AttackCalculator.getAttackValues("Jeep"))
    ),
    _LIGHT_TANK(
            "flavor.unit.lightTank.name",
            "flavor.unit.lightTank.description",
            UnitTypeInfo.class.getResource("/assets/unit/portrait/skeleton.png"),
            UnitTypeInfo.class.getResource("/assets/unit/icon/sword-wound.png"),
            new CanAttack(AttackCalculator.getAttackValues("LightTank"))
    ),
    _HEAVY_TANK(
            "flavor.unit.heavyTank.name",
            "flavor.unit.heavyTank.description",
            UnitTypeInfo.class.getResource("/assets/unit/portrait/chubby-transparent.gif"),
            UnitTypeInfo.class.getResource("/assets/unit/icon/dwarf-face.png"),
            new CanAttack(AttackCalculator.getAttackValues("HeavyTank"))
    ),
    _CHOPPER(
            "flavor.unit.chopper.name",
            "flavor.unit.chopper.description",
            UnitTypeInfo.class.getResource("/assets/unit/portrait/bird.gif"),
            UnitTypeInfo.class.getResource("/assets/unit/icon/dragon-head.png"),
            new CanAttack(AttackCalculator.getAttackValues("Chopper"))
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

    public int getCanAttack(String name) {
        return canAttack.getAttackValue(name);
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

    private static class CanAttack {

        private final HashMap<String, Integer> attackValue = new HashMap<>();

        CanAttack(int[] values) {
            attackValue.put("flavor.unit.infantry.name", values[0]);
            attackValue.put("flavor.unit.bazookaTrooper.name", values[1]);
            attackValue.put("flavor.unit.jeep.name", values[2]);
            attackValue.put("flavor.unit.lightTank.name", values[3]);
            attackValue.put("flavor.unit.heavyTank.name", values[4]);
            attackValue.put("flavor.unit.chopper.name", values[5]);
        }

        private int getAttackValue(String name) {
            return attackValue.get(name);
        }

    }
}
