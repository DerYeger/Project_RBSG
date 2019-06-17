package de.uniks.se19.team_g.project_rbsg.waiting_room.model;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

public class ModelTests {

    private static final String ALICE = "Alice";
    private static final String BOB = "Bob";

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
                .setUnitType(UnitType.JEEP)
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

        assertEquals(game, forest.getGame());
        assertEquals(Biome.FOREST, forest.getBiome());
        assertTrue(forest.isPassable());
        assertEquals(0, forest.getX());
        assertEquals(0, forest.getY());
        assertNull(forest.getLeft());
        assertNull(forest.getTop());
        assertEquals(grass, forest.getRight());
        assertEquals(mountain, forest.getBottom());
        assertNull(forest.getUnit().get());

        assertEquals(game, grass.getGame());
        assertEquals(Biome.GRASS, grass.getBiome());
        assertTrue(grass.isPassable());
        assertEquals(1, grass.getX());
        assertEquals(0, grass.getY());
        assertEquals(forest, grass.getLeft());
        assertNull(grass.getTop());
        assertNull(grass.getRight());
        assertEquals(water, grass.getBottom());
        assertEquals(aliceJeep1And5, grass.getUnit().get());

        assertEquals(game, mountain.getGame());
        assertEquals(Biome.MOUNTAIN, mountain.getBiome());
        assertFalse(mountain.isPassable());
        assertEquals(0, mountain.getX());
        assertEquals(1, mountain.getY());
        assertNull(mountain.getLeft());
        assertEquals(forest, mountain.getTop());
        assertEquals(water, mountain.getRight());
        assertNull(mountain.getBottom());
        assertEquals(bobChopper10And10, mountain.getUnit().get());

        assertEquals(game, water.getGame());
        assertEquals(Biome.WATER, water.getBiome());
        assertFalse(water.isPassable());
        assertEquals(1, water.getX());
        assertEquals(1, water.getY());
        assertEquals(mountain, water.getLeft());
        assertEquals(grass, water.getTop());
        assertNull(water.getRight());
        assertNull(water.getBottom());
        assertEquals(aliceChopper5And10, water.getUnit().get());

        assertEquals(game, alice.getGame());
        assertEquals(ALICE, alice.getName());
        assertEquals(RED, alice.getColor());
        assertTrue(alice.getUnits().containsAll(Arrays.asList(aliceChopper5And10, aliceJeep1And5)));
        assertEquals(2, alice.getUnits().size());

        assertEquals(game, bob.getGame());
        assertEquals(BOB, bob.getName());
        assertEquals(BLUE, bob.getColor());
        assertTrue(bob.getUnits().contains(bobChopper10And10));
        assertEquals(1, bob.getUnits().size());

        assertEquals(game, aliceChopper5And10.getGame());
        assertEquals(alice, aliceChopper5And10.getLeader());
        assertEquals(water, aliceChopper5And10.getPosition().get());
        assertEquals(UnitType.CHOPPER, aliceChopper5And10.getUnitType());
        assertEquals(5, aliceChopper5And10.getMp());
        assertEquals(10, aliceChopper5And10.getHp());
        assertEquals(canAttack, aliceChopper5And10.getCanAttack());

        assertEquals(game, aliceJeep1And5.getGame());
        assertEquals(alice, aliceJeep1And5.getLeader());
        assertEquals(grass, aliceJeep1And5.getPosition().get());
        assertEquals(UnitType.JEEP, aliceJeep1And5.getUnitType());
        assertEquals(1, aliceJeep1And5.getMp());
        assertEquals(5, aliceJeep1And5.getHp());
        assertEquals(canAttack, aliceJeep1And5.getCanAttack());

        assertEquals(game, bobChopper10And10.getGame());
        assertEquals(bob, bobChopper10And10.getLeader());
        assertEquals(mountain, bobChopper10And10.getPosition().get());
        assertEquals(UnitType.CHOPPER, bobChopper10And10.getUnitType());
        assertEquals(10, bobChopper10And10.getMp());
        assertEquals(10, bobChopper10And10.getHp());
        assertEquals(canAttack, bobChopper10And10.getCanAttack());

        //action
        aliceJeep1And5.remove();

        //asserts
        assertFalse(game.getUnits().contains(aliceJeep1And5));
        assertEquals(2, game.getUnits().size());

        assertNull(grass.getUnit().get());

        assertFalse(alice.getUnits().contains(aliceJeep1And5));
        assertEquals(1, alice.getUnits().size());

        assertNull(aliceJeep1And5.getGame());
        assertNull(aliceJeep1And5.getLeader());
        assertNull(aliceJeep1And5.getPosition().get());
        assertEquals(UnitType.JEEP, aliceJeep1And5.getUnitType());
        assertEquals(1, aliceJeep1And5.getMp());
        assertEquals(5, aliceJeep1And5.getHp());
        assertEquals(canAttack, aliceJeep1And5.getCanAttack());


        //action
        water.remove();

        //asserts
        assertFalse(game.getCells().contains(water));
        assertEquals(3, game.getCells().size());

        assertNull(water.getGame());
        assertEquals(Biome.WATER, water.getBiome());
        assertFalse(water.isPassable());
        assertEquals(1, water.getX());
        assertEquals(1, water.getY());
        assertNull(water.getLeft());
        assertNull(water.getTop());
        assertNull(water.getRight());
        assertNull(water.getBottom());
        assertNull(water.getUnit().get());

        assertNull(mountain.getRight());
        assertNull(grass.getBottom());

        //action
        bob.remove();

        //asserts
        assertFalse(game.getPlayers().contains(bob));
        assertEquals(1, game.getPlayers().size());

        assertNull(bobChopper10And10.getLeader());

        //action
        game.remove();

        //asserts
        assertTrue(game.getPlayers().isEmpty());
        assertTrue(game.getUnits().isEmpty());
        assertTrue(game.getCells().isEmpty());

        assertNull(forest.getGame());
        assertNull(grass.getGame());
        assertNull(mountain.getGame());
        assertNull(water.getGame());

        assertNull(alice.getGame());
        assertNull(bob.getGame());

        assertNull(aliceChopper5And10.getGame());
        assertNull(aliceJeep1And5.getGame());
        assertNull(bobChopper10And10.getGame());
    }
}
