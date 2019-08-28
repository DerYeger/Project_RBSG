package de.uniks.se19.team_g.project_rbsg.util;

import de.uniks.se19.team_g.project_rbsg.configuration.flavor.UnitTypeInfo;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Biome;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import java.util.HashMap;

/**
 *
 * @author Keanu Stückrad
 *
 *  The following part shows the attributes that were given by the game server
 *  but they weren't send to the clients! It's hardcoded therefore it has to be changed
 *  if the server provider decides to change some attributes
 *
 *  Field-Defense:
 *  Grass 1
 *  Forest 3
 *  Mountain 4
 *
 *  Attack      Infantry    Bazooka     Jeep    Light Tank      Heavy Tank      Chopper
 *  Infantry 	55          45          12      5               1               -
 *  Bazooka 	-           -           85      55              15              55
 *  Jeep        70          65          35      -               -               -
 *  Light Tank 	75          70          85      55              15              -
 *  Heavy Tank 	105	        95          105     85              55              75
 *  Chopper 	75          75          55      25              20              -
 */
public class AttackCalculator {

    private static final Logger logger = LoggerFactory.getLogger(AttackCalculator.class);


    /**
     *  damage = (attack - defense) % 10
     *  minimum damage is always 1
     */
    public static int calculateDamage(@NonNull final Unit agressor, @NonNull final Unit defender, boolean log) {
        int attack = getAttackValue(agressor, defender);
        int defense = calculateDefense(defender);
        int damage = (attack - defense) % 10;
        int damageWithoutZero = damage <= 0 ? 1 : damage;
        if(log) logger.info(
                "ATTACK LOG:" +
                "\nUnitType Agressor: " + agressor.getUnitType() +
                ", X: " + agressor.getPosition().getX() +
                ", Y: " + agressor.getPosition().getY() +
                ", Attack: " + attack +
                "\nUnitType Defender: " + defender.getUnitType() +
                ", X: " + defender.getPosition().getX() +
                ", Y: " + defender.getPosition().getY() +
                ", HP: " + defender.getHp() +
                ", Biom: " + defender.getPosition().getBiome() +
                ", Defense: " + defense +
                "\nDamage: " + damageWithoutZero +
                ", Remaining HP: " + (defender.getHp() - damageWithoutZero)
        );
        return damageWithoutZero;
    }

    /**
     *  defense = hp / 10 * (fieldDefense * 10)
     */
    private static int calculateDefense(@NonNull final Unit defender) {
        Biome biome = defender.getPosition().getBiome();
        int hp = defender.getHp();
        int fieldDefense = biome.equals(Biome.GRASS) ? 1 : biome.equals(Biome.FOREST) ? 3 : 4;
        return hp / 10 * (fieldDefense * 10);
    }

    private static int getAttackValue(@NonNull final Unit agressor, @NonNull final Unit defender){
        return CanAttack.mapCanAttackTo(agressor.getUnitType()).getAttack(defender.getUnitType());
    }

    public static int getAttackValue(@NonNull final de.uniks.se19.team_g.project_rbsg.model.Unit agressor, @NonNull final de.uniks.se19.team_g.project_rbsg.model.Unit defender){
        return CanAttack.mapCanAttackTo(agressor.getTypeInfo()).getAttack(defender.getTypeInfo());
    }

    public static int getAttackValue(@NonNull final Unit agressor, @NonNull final UnitTypeInfo unitTypeInfoDefender){
        return CanAttack.mapCanAttackTo(agressor.getUnitType()).getAttack(unitTypeInfoDefender);
    }

    private enum CanAttack {

        /**
         *  hardcoded attack values from table above
         */
        _INFANTRY(
                55, 45, 12, 5, 1, 0
        ),
        _BAZOOKA_TROOPER(
                0, 0, 85, 55, 15, 55
        ),
        _JEEP(
                70, 65, 35, 0, 0, 0
        ),
        _LIGHT_TANK(
                75, 70, 85, 55, 15, 0
        ),
        _HEAVY_TANK(
                105, 95, 105, 85, 55, 75
        ),
        _CHOPPER(
                75, 75, 55, 25, 20, 0
        ),
        ;

        private final HashMap<UnitTypeInfo, Integer> attackValue = new HashMap<>();

        CanAttack(int a0, int a1, int a2, int a3, int a4, int a5) {
            attackValue.put(UnitTypeInfo._INFANTRY, a0);
            attackValue.put(UnitTypeInfo._BAZOOKA_TROOPER, a1);
            attackValue.put(UnitTypeInfo._JEEP, a2);
            attackValue.put(UnitTypeInfo._LIGHT_TANK, a3);
            attackValue.put(UnitTypeInfo._HEAVY_TANK, a4);
            attackValue.put(UnitTypeInfo._CHOPPER, a5);
        }

        // Map UnitTypeInfos to CanAttacks
        private static CanAttack mapCanAttackTo(@NonNull final UnitTypeInfo unitTypeInfo){
            switch (unitTypeInfo) {
                case _INFANTRY:
                    return CanAttack._INFANTRY;
                case _BAZOOKA_TROOPER:
                    return CanAttack._BAZOOKA_TROOPER;
                case _JEEP:
                    return CanAttack._JEEP;
                case _LIGHT_TANK:
                    return CanAttack._LIGHT_TANK;
                case _HEAVY_TANK:
                    return CanAttack._HEAVY_TANK;
                case _CHOPPER:
                    return CanAttack._CHOPPER;
                default:
                    return null;
            }
        }

        private int getAttack(@NonNull final UnitTypeInfo unitTypeInfo) {
            return attackValue.get(unitTypeInfo);
        }

    }

}
