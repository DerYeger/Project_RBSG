package de.uniks.se19.team_g.project_rbsg.configuration.flavor;

import javafx.scene.image.*;

import java.net.*;
import java.util.*;

public class UnitImageResolver
{
    private static FlavourType flavour = FlavourType.DEFAULT;

    private static HashMap<UnitTypeInfo, Image> unitTypeInfoImageHashMap = new HashMap<>();

    public static Image getUnitImage(UnitTypeInfo unitType) {
        if(!unitTypeInfoImageHashMap.containsKey(unitType)) {
            loadImageForType(unitType);
        }

        return unitTypeInfoImageHashMap.get(unitType);
    }

    public static void setFlavour(FlavourType flavourType) {
        flavour = flavourType;

        for (UnitTypeInfo unitInfo : UnitTypeInfo.values())
        {
            loadImageForType(unitInfo);
        }
    }

    public static URL getUnitImageURL(UnitTypeInfo unitType) {
        switch (unitType) {

            case UNKNOWN:
                return getUnknownUrl();
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
        switch (flavour) {
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
        switch (flavour) {
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
        switch (flavour) {
            case DEFAULT:
                return UnitImageResolver.class.getResource("/assets/unit/portrait/skeleton.png");
            case WH40K:
                return UnitImageResolver.class.getResource("/assets/unit/portrait/WH40K/Dreadnaught.png");
            default:
                return UnitImageResolver.class.getResource("/assets/unit/portrait/mr-unknown.png");
        }
    }

    private static URL getJeepUrl ()
    {
        switch (flavour) {
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
        switch (flavour) {
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
        switch (flavour) {

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
        switch (unitType) {
            case UNKNOWN:
                LoadUnknown();
                break;
            case _INFANTRY:
                LoadInfantry();
                break;
            case _BAZOOKA_TROOPER:
                LoadBazooka();
                break;
            case _JEEP:
                LoadJeep();
                break;
            case _LIGHT_TANK:
                LoadLightTank();
                break;
            case _HEAVY_TANK:
                LoadHeavyTank();
                break;
            case _CHOPPER:
                LoadChopper();
                break;
        }
    }

    private static void LoadUnknown ()
    {
        URL imageUrl = null;

        imageUrl = getUnknownUrl();

        unitTypeInfoImageHashMap.put(UnitTypeInfo._CHOPPER, new Image(imageUrl.toExternalForm()));
    }

    private static void LoadChopper ()
    {
        URL imageUrl = null;

        imageUrl = getChopperUrl();

        unitTypeInfoImageHashMap.put(UnitTypeInfo._CHOPPER, new Image(imageUrl.toExternalForm()));
    }

    private static void LoadHeavyTank ()
    {
        URL imageUrl = null;

        imageUrl = getHeavyTankUrl();

        unitTypeInfoImageHashMap.put(UnitTypeInfo._HEAVY_TANK, new Image(imageUrl.toExternalForm()));
    }

    private static void LoadLightTank ()
    {
        URL imageUrl = null;

        imageUrl = getLightTankUrl();

        unitTypeInfoImageHashMap.put(UnitTypeInfo._LIGHT_TANK, new Image(imageUrl.toExternalForm()));
    }

    private static void LoadJeep ()
    {
        URL imageUrl = null;

        imageUrl = getJeepUrl();

        unitTypeInfoImageHashMap.put(UnitTypeInfo._JEEP, new Image(imageUrl.toExternalForm()));
    }

    private static void LoadBazooka ()
    {
        URL imageUrl = null;

        imageUrl = getBazookaTrooperUrl();

        unitTypeInfoImageHashMap.put(UnitTypeInfo._BAZOOKA_TROOPER, new Image(imageUrl.toExternalForm()));
    }

    private static void LoadInfantry ()
    {
        URL imageUrl = null;

        imageUrl = getInfantryUrl();

        unitTypeInfoImageHashMap.put(UnitTypeInfo._INFANTRY, new Image(imageUrl.toExternalForm()));
    }


}
