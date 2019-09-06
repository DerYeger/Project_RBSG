package de.uniks.se19.team_g.project_rbsg.ingame.model;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.uniks.se19.team_g.project_rbsg.configuration.flavor.UnitTypeInfo;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author Jan Müller
 */
public class ModelManagerTests {

    @Test
    public void testGameInitAndRemove() throws IOException {
        final String gameMessage = "{\"action\":\"gameInitObject\",\"data\":{\"id\":\"Game@1\",\"allPlayer\":[\"Player@1\"],\"allUnits\":[\"Unit@1\",\"Unit@2\"]}}";

        final String playerMessage = "{\"action\":\"gameInitObject\",\"data\":{\"id\":\"Player@1\",\"name\":\"ggEngineering\",\"color\":\"RED\",\"currentGame\":\"Game@1\",\"army\":[\"Unit@1\",\"Unit@2\"]}}";

        final String forestCellMessage = "{\"action\":\"gameInitObject\",\"data\":{\"id\":\"Forest@1\",\"game\":\"Game@1\",\"x\":0,\"y\":0,\"isPassable\":true,\"right\":\"Grass@1\",\"bottom\":\"Mountain@1\"}}";
        final String grassCellMessage = "{\"action\":\"gameInitObject\",\"data\":{\"id\":\"Grass@1\",\"game\":\"Game@1\",\"x\":1,\"y\":0,\"isPassable\":true,\"left\":\"Forest@1\",\"bottom\":\"Water@1\"}}";
        final String mountainCellMessage = "{\"action\":\"gameInitObject\",\"data\":{\"id\":\"Mountain@1\",\"game\":\"Game@1\",\"x\":0,\"y\":1,\"isPassable\":false,\"top\":\"Forest@1\",\"right\":\"Water@1\"}}";
        final String waterCellMessage = "{\"action\":\"gameInitObject\",\"data\":{\"id\":\"Water@1\",\"game\":\"Game@1\",\"x\":1,\"y\":1,\"isPassable\":false,\"left\":\"Mountain@1\",\"top\":\"Grass@1\"}}";

        final String firstUnitMessage = "{\"action\":\"gameInitObject\",\"data\":{\"id\":\"Unit@1\",\"type\":\"Chopper\",\"mp\":5,\"hp\":10,\"canAttack\":[\"Light Tank\",\"Heavy Tank\"],\"game\":\"Game@1\",\"leader\":\"Player@1\",\"position\":\"Water@1\"}}";
        final String secondUnitMessage = "{\"action\":\"gameInitObject\",\"data\":{\"id\":\"Unit@2\",\"type\":\"Jeep\",\"mp\":1,\"hp\":5,\"canAttack\":[\"Infantry\"],\"game\":\"Game@1\",\"leader\":\"Player@1\",\"position\":\"Grass@1\"}}";

        final ModelManager modelManager = new ModelManager();
        // execute synchronous
        modelManager.setExecutor(Runnable::run);

        final ObjectMapper mapper = new ObjectMapper();

        modelManager.handle(mapper.readValue(secondUnitMessage, ObjectNode.class));
        modelManager.handle(mapper.readValue(mountainCellMessage, ObjectNode.class));
        modelManager.handle(mapper.readValue(forestCellMessage, ObjectNode.class));
        modelManager.handle(mapper.readValue(gameMessage, ObjectNode.class));
        modelManager.handle(mapper.readValue(playerMessage, ObjectNode.class));
        modelManager.handle(mapper.readValue(grassCellMessage, ObjectNode.class));
        modelManager.handle(mapper.readValue(firstUnitMessage, ObjectNode.class));
        modelManager.handle(mapper.readValue(waterCellMessage, ObjectNode.class));

        final Game game = modelManager.getGame();

        assertNotNull(game);
        assertEquals("Game@1", game.getId());
        assertEquals(1, game.getPlayers().size());
        assertEquals(2, game.getUnits().size());
        assertEquals(4, game.getCells().size());

        final Player player = game.getPlayers().get(0);

        assertNotNull(player);
        assertEquals("Player@1", player.getId());
        assertEquals(game, player.getCurrentGame());
        assertEquals("ggEngineering", player.getName());
        assertEquals("RED", player.getColor());
        assertEquals(2, player.getUnits().size());
        assertTrue(player.getUnits().containsAll(game.getUnits()));

        final Unit chopper = player.getUnits().stream().filter(u -> u.getUnitType().equals(UnitTypeInfo._CHOPPER)).findAny().orElse(null);

        assertNotNull(chopper);
        assertEquals("Unit@1", chopper.getId());
        assertEquals(game, chopper.getGame());
        assertEquals(player, chopper.getLeader());
        assertEquals(UnitTypeInfo._CHOPPER, chopper.getUnitType());
        assertEquals(5, chopper.getMp());
        assertEquals(10, chopper.getHp());
        assertEquals(Arrays.asList(UnitTypeInfo._LIGHT_TANK, UnitTypeInfo._HEAVY_TANK), chopper.getCanAttack());

        final Unit jeep = player.getUnits().stream().filter(u -> u.getUnitType().equals(UnitTypeInfo._JEEP)).findAny().orElse(null);

        assertNotNull(jeep);
        assertEquals("Unit@2", jeep.getId());
        assertEquals(game, jeep.getGame());
        assertEquals(player, jeep.getLeader());
        assertEquals(UnitTypeInfo._JEEP, jeep.getUnitType());
        assertEquals(1, jeep.getMp());
        assertEquals(5, jeep.getHp());
        assertEquals(Collections.singletonList(UnitTypeInfo._INFANTRY), jeep.getCanAttack());

        final Cell forest = game.getCells().stream().filter(c -> c.getBiome().equals(Biome.FOREST)).findAny().orElse(null);
        final Cell grass = game.getCells().stream().filter(c -> c.getBiome().equals(Biome.GRASS)).findAny().orElse(null);
        final Cell mountain = game.getCells().stream().filter(c -> c.getBiome().equals(Biome.MOUNTAIN)).findAny().orElse(null);
        final Cell water = game.getCells().stream().filter(c -> c.getBiome().equals(Biome.WATER)).findAny().orElse(null);

        assertNotNull(forest);
        assertNotNull(grass);
        assertNotNull(mountain);
        assertNotNull(water);

        assertEquals(game, forest.getGame());
        assertEquals("Forest@1", forest.getId());
        assertEquals(Biome.FOREST, forest.getBiome());
        assertTrue(forest.isPassable());
        assertEquals(0, forest.getX());
        assertEquals(0, forest.getY());
        assertNull(forest.getLeft());
        assertNull(forest.getTop());
        assertEquals(grass, forest.getRight());
        assertEquals(mountain, forest.getBottom());
        assertNull(forest.unitProperty().get());

        assertEquals(game, grass.getGame());
        assertEquals("Grass@1", grass.getId());
        assertEquals(Biome.GRASS, grass.getBiome());
        assertTrue(grass.isPassable());
        assertEquals(1, grass.getX());
        assertEquals(0, grass.getY());
        assertEquals(forest, grass.getLeft());
        assertNull(grass.getTop());
        assertNull(grass.getRight());
        assertEquals(water, grass.getBottom());
        assertEquals(jeep, grass.unitProperty().get());

        assertEquals(game, mountain.getGame());
        assertEquals("Mountain@1", mountain.getId());
        assertEquals(Biome.MOUNTAIN, mountain.getBiome());
        assertFalse(mountain.isPassable());
        assertEquals(0, mountain.getX());
        assertEquals(1, mountain.getY());
        assertNull(mountain.getLeft());
        assertEquals(forest, mountain.getTop());
        assertEquals(water, mountain.getRight());
        assertNull(mountain.getBottom());
        assertNull(mountain.unitProperty().get());

        assertEquals(game, water.getGame());
        assertEquals("Water@1", water.getId());
        assertEquals(Biome.WATER, water.getBiome());
        assertFalse(water.isPassable());
        assertEquals(1, water.getX());
        assertEquals(1, water.getY());
        assertEquals(mountain, water.getLeft());
        assertEquals(grass, water.getTop());
        assertNull(water.getRight());
        assertNull(water.getBottom());
        assertEquals(chopper, water.unitProperty().get());

        assertEquals(water, chopper.positionProperty().get());
        assertEquals(grass, jeep.positionProperty().get());
    }
}
