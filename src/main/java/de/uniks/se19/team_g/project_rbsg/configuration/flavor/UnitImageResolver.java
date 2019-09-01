package de.uniks.se19.team_g.project_rbsg.configuration.flavor;

import de.uniks.se19.team_g.project_rbsg.server.rest.army.units.*;
import javafx.beans.property.*;
import javafx.scene.image.*;
import javafx.util.*;

import java.net.*;
import java.util.*;

public class UnitImageResolver
{
    private static SimpleObjectProperty<FlavourType> flavour = new SimpleObjectProperty<>(FlavourType.DEFAULT);

    private static HashMap<UnitTypeInfo, SimpleObjectProperty<Image>> unitTypeInfoImageHashMap = new HashMap<>();

    private static HashMap<String, Image> imageWithSizeMap = new HashMap<>();

    private static Image attackImage = null;

    private static Image revivalImage = null;

    private static Image deathImage = null;

    public static Image getAttackImage() {
        if(attackImage == null) {
            attackImage = new Image(UnitImageResolver.class.getResource("/assets/unit/effect/attack.gif").toExternalForm() , 64, 64,
                                    false, true);
        }

        return attackImage;
    }

    public static Image getRevivalImage() {
        if(revivalImage == null) {
            revivalImage = new Image(UnitImageResolver.class.getResource("/assets/unit/effect/energy_ball.gif").toExternalForm() , 64, 64,
                                     false, true);
        }

        return revivalImage;
    }

    public static Image getDeathImage() {
        if(deathImage == null) {
            deathImage = new Image(UnitImageResolver.class.getResource("/assets/unit/effect/explosion.gif").toExternalForm() ,
                          64, 64,
                                     false, true);
        }

        return deathImage;
    }

    public static SimpleObjectProperty<Image> getUnitImageProperty(UnitTypeInfo unitType) {
        if(!unitTypeInfoImageHashMap.containsKey(unitType)) {
            loadImageForType(unitType);
        }

        return unitTypeInfoImageHashMap.get(unitType);
    }

    public static Image getUnitImage(UnitTypeInfo unitType) {
        if(!unitTypeInfoImageHashMap.containsKey(unitType)) {
            loadImageForType(unitType);
        }

        return unitTypeInfoImageHashMap.get(unitType).get();
    }

    public static void setFlavour(FlavourType flavourType) {
        flavour.set(flavourType);

        for (UnitTypeInfo unitInfo : UnitTypeInfo.values())
        {
            loadImageForType(unitInfo);
        }
    }

    public static FlavourType getFlavour() {
        return flavour.get();
    }

    public static ObjectProperty<FlavourType> flavourProperty() {
        return flavour;
    }

    public static URL getUnitImageURL(UnitTypeInfo unitType) {
        switch (unitType) {
            case _INFANTRY:
                return getInfantryUrl();
            case _BAZOOKA_TROOPER:
                return  getBazookaTrooperUrl();
            case _JEEP:
                return getJeepUrl();
            case _LIGHT_TANK:
                return getLightTankUrl();
            case _HEAVY_TANK:
                return getHeavyTankUrl();
            case _CHOPPER:
                return getChopperUrl();
            default:
                return getUnknownUrl();
        }
    }

    private static URL getUnknownUrl ()
    {
        return UnitImageResolver.class.getResource("/assets/unit/portrait/mr-unknown.png");
    }

    private static URL getChopperUrl ()
    {
        switch (flavour.get()) {
            case DEFAULT:
                return UnitImageResolver.class.getResource("/assets/unit/portrait/bird.gif");
            case WH40K:
                return UnitImageResolver.class.getResource("/assets/unit/portrait/WH40K/SpaceMarinePlasmaGun.gif");
            default:
                return UnitImageResolver.class.getResource("/assets/unit/portrait/mr-unknown.png");
        }
    }

    private static URL getHeavyTankUrl ()
    {
        switch (flavour.get()) {
            case DEFAULT:
                return UnitImageResolver.class.getResource("/assets/unit/portrait/chubby-transparent.gif");
            case WH40K:
                return UnitImageResolver.class.getResource("/assets/unit/portrait/WH40K/LemanRuss.gif");
            default:
                return UnitImageResolver.class.getResource("/assets/unit/portrait/mr-unknown.png");
        }
    }

    private static URL getLightTankUrl ()
    {
        switch (flavour.get()) {
            case DEFAULT:
                return UnitImageResolver.class.getResource("/assets/unit/portrait/undead/undead_walk.gif");
            case WH40K:
                return UnitImageResolver.class.getResource("/assets/unit/portrait/WH40K/Dreadnaught.png");
            default:
                return UnitImageResolver.class.getResource("/assets/unit/portrait/mr-unknown.png");
        }
    }

    private static URL getJeepUrl ()
    {
        switch (flavour.get()) {
            case DEFAULT:
                return UnitImageResolver.class.getResource("/assets/unit/portrait/mr-unknown.png");
            case WH40K:
                return UnitImageResolver.class.getResource("/assets/unit/portrait/WH40K/SpaceMarineFlamer.gif");
            default:
                return UnitImageResolver.class.getResource("/assets/unit/portrait/mr-unknown.png");
        }
    }

    private static URL getBazookaTrooperUrl ()
    {
        switch (flavour.get()) {
            case DEFAULT:
                return UnitImageResolver.class.getResource("/assets/unit/portrait/khorneberzerker.png");
            case WH40K:
                return UnitImageResolver.class.getResource("/assets/unit/portrait/WH40K/SpaceMarineRocketLauncher.gif");
            default:
                return UnitImageResolver.class.getResource("/assets/unit/portrait/mr-unknown.png");
        }
    }

    private static URL getInfantryUrl ()
    {
        switch (flavour.get()) {

            case DEFAULT:
                return UnitImageResolver.class.getResource("/assets/unit/portrait/soldier.gif");
            case WH40K:
                return UnitImageResolver.class.getResource("/assets/unit/portrait/WH40K/SpaceMarineChainSword.gif");
            default:
                return UnitImageResolver.class.getResource("/assets/unit/portrait/mr-unknown.png");
        }
    }

    private static void loadImageForType (UnitTypeInfo unitType)
    {
        URL imageUrl = getUnitUrl(unitType);

        if(!unitTypeInfoImageHashMap.containsKey(unitType)) {
            unitTypeInfoImageHashMap.put(unitType, new SimpleObjectProperty<>());
        }

        unitTypeInfoImageHashMap.get(unitType).set(new Image(imageUrl.toExternalForm()));
    }

    private static URL getUnitUrl (UnitTypeInfo unitType)
    {
        URL imageUrl = null;

        switch (unitType)
        {
            case UNKNOWN:
                imageUrl = getUnknownUrl();
                break;
            case _INFANTRY:
                imageUrl = getInfantryUrl();
                break;
            case _BAZOOKA_TROOPER:
                imageUrl = getBazookaTrooperUrl();
                break;
            case _JEEP:
                imageUrl = getJeepUrl();
                break;
            case _LIGHT_TANK:
                imageUrl = getLightTankUrl();
                break;
            case _HEAVY_TANK:
                imageUrl = getHeavyTankUrl();
                break;
            case _CHOPPER:
                imageUrl = getChopperUrl();
                break;
        }
        return imageUrl;
    }

    public static Image getUnitImage (UnitTypeInfo unitTypeInfo, int height, int width)
    {
        String key = toKey(unitTypeInfo, height, width);

        if(!imageWithSizeMap.containsKey(key)) {
            loadImageWithSize(unitTypeInfo, height, width);
        }

        return imageWithSizeMap.get(key);
    }

    private static void loadImageWithSize (UnitTypeInfo unitType, int height, int width)
    {
        URL imageUrl = getUnitUrl(unitType);

        imageWithSizeMap.put(toKey(unitType, height, width), new Image(imageUrl.toExternalForm(), width, height,
                                                                       false, true));
    }

    private static String toKey(UnitTypeInfo unitType, int height, int width) {
        return String.format("%s_%s_%d_%d", unitType.toString(), flavour.get().toString(), height, width);
    }
}
