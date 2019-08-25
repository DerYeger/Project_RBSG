package de.uniks.se19.team_g.project_rbsg.util;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Biome;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
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
 *
 *  The attack is hardcoded in the de/uniks/se19/team_g/project_rbsg/configuration/flavor/UnitTypeInfo.java
 *  And can be accessed by writing attackingUnit.getAttackValue(defendingUnit)
 *  See the usage below in line 34
 */
public class AttackCalculator {

    private static final Logger logger = LoggerFactory.getLogger(AttackCalculator.class);

    /**
     *  damage = (attack - defense) % 10
     *  minimum damage is always 1
     */
    public static int calculateDamage(Unit agressor, Unit defender, boolean log) {
        int attack = agressor.getAttackValue(defender);
        int defense = calculateDefense(defender);
        int damage = (attack - defense) % 10;
        int damageWithoutZero = damage == 0 ? 1 : damage;
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
    private static int calculateDefense(Unit defender) {
        Biome biome = defender.getPosition().getBiome();
        int hp = defender.getHp();
        int fieldDefense = biome.equals(Biome.GRASS) ? 1 : biome.equals(Biome.FOREST) ? 3 : 4;
        return hp / 10 * (fieldDefense * 10);
    }

}
