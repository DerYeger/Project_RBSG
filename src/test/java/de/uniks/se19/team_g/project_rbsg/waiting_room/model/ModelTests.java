package de.uniks.se19.team_g.project_rbsg.waiting_room.model;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

public class ModelTests {

    private static final String ALICE = "alice";
    private static final String BOB = "bob";

    private static final String RED = "RED";
    private static final String BLUE = "BLUE";

    @Test
    public void testAssociations() {
        //init
        final Game game = new Game("");

        final Cell forest = new Cell("")
                .setBiome(Biome.FOREST)
                .setPassable(true)
                .setX(0)
                .setY(0);
        final Cell grass = new Cell("")
                .setBiome(Biome.GRASS)
                .setPassable(true)
                .setX(1)
                .setY(0);
        final Cell mountain = new Cell("")
                .setBiome(Biome.MOUNTAIN)
                .setPassable(false)
                .setX(0)
                .setY(1);
        final Cell water = new Cell("")
                .setBiome(Biome.WATER)
                .setPassable(false)
                .setX(1)
                .setY(1);

        forest.setRight(grass)
                .setBottom(mountain);
        water.setTop(grass)
                .setLeft(mountain);

        game.withCells(forest, grass, null, water);
        mountain.setGame(game);

        final Player alice = new Player("")
                .setName(ALICE)
                .setColor(RED);
        final Player bob = new Player("")
                .setName(BOB)
                .setColor(BLUE);

        game.withPlayers(null, alice);
        bob.setGame(game);

        final Collection<UnitType> canAttack = Arrays.asList(UnitType.CHOPPER, UnitType.JEEP);

        final Unit aliceChopper5And10 = new Unit("")
                .setUnitType(UnitType.CHOPPER)
                .setMp(5)
                .setHp(10)
                .setCanAttack(canAttack);
        final Unit aliceJeep1And5 = new Unit("")
                .setUnitType(UnitType.CHOPPER)
                .setMp(1)
                .setHp(5)
                .setCanAttack(canAttack);
        final Unit bobChopper10And10 = new Unit("")
                .setUnitType(UnitType.CHOPPER)
                .setMp(10)
                .setHp(10)
                .setCanAttack(canAttack);

        game.withUnits(aliceChopper5And10, aliceJeep1And5);

        alice.withUnits(aliceChopper5And10, aliceJeep1And5);

        bobChopper10And10.setGame(game)
                .setLeader(bob);

        aliceChopper5And10.setPosition(water);
        aliceJeep1And5.setPosition(grass);
        mountain.setUnit(bobChopper10And10);

        //asserts
        assertTrue(game.getPlayers().containsAll(Arrays.asList(alice, bob)));
        assertEquals(2, game.getPlayers().size());
        assertTrue(game.getUnits().containsAll(Arrays.asList(aliceChopper5And10, aliceJeep1And5, bobChopper10And10)));
        assertEquals(3, game.getUnits().size());
        assertTrue(game.getCells().containsAll(Arrays.asList(forest, grass, mountain, water)));
        assertEquals(4, game.getCells().size());

        assertEquals(game, alice.getGame());
        assertEquals(game, bob.getGame());
        assertEquals(game, forest.getGame());
        assertEquals(game, grass.getGame());
        assertEquals(game, mountain.getGame());
        assertEquals(game, water.getGame());
        assertEquals(game, aliceChopper5And10.getGame());
        assertEquals(game, aliceJeep1And5.getGame());
        assertEquals(game, bobChopper10And10.getGame());

        assertEquals(Biome.FOREST, forest.getBiome());
        assertTrue(forest.isPassable());
        assertEquals(0, forest.getX());
        assertEquals(0, forest.getY());
        assertNull(forest.getLeft());
        assertNull(forest.getTop());
        assertEquals(grass, forest.getRight());
        assertEquals(mountain, forest.getBottom());

        assertEquals(Biome.GRASS, grass.getBiome());
        assertTrue(grass.isPassable());
        assertEquals(1, grass.getX());
        assertEquals(0, grass.getY());
        assertEquals(forest, grass.getLeft());
        assertNull(grass.getTop());
        assertNull(grass.getRight());
        assertEquals(water, grass.getBottom());

        assertEquals(Biome.MOUNTAIN, mountain.getBiome());
        assertFalse(mountain.isPassable());
        assertEquals(0, mountain.getX());
        assertEquals(1, mountain.getY());
        assertNull(mountain.getLeft());
        assertEquals(forest, mountain.getTop());
        assertEquals(water, mountain.getRight());
        assertNull(mountain.getBottom());

        assertEquals(Biome.WATER, water.getBiome());
        assertFalse(water.isPassable());
        assertEquals(1, water.getX());
        assertEquals(1, water.getY());
        assertEquals(mountain, water.getLeft());
        assertEquals(grass, water.getTop());
        assertNull(water.getRight());
        assertNull(water.getBottom());
    }
}
