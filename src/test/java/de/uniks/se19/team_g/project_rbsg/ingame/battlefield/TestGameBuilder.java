package de.uniks.se19.team_g.project_rbsg.ingame.battlefield;

import de.uniks.se19.team_g.project_rbsg.configuration.flavor.UnitTypeInfo;
import de.uniks.se19.team_g.project_rbsg.ingame.model.*;
import org.springframework.lang.NonNull;

import java.util.Arrays;
import java.util.Collections;

public class TestGameBuilder {

    /**
        Y -> player unit, 0 -> passable, X -> blocked, E -> enemy
        OYOO
        XXEO
        -OOO
        --O-
     */
    public static Definition sampleGameAlpha() {
        Definition definition = new Definition(new Cell[4][4]);

        Game game = definition.game;
        Unit chubbyCharles = definition.playerUnit;
        definition.playerUnit.setUnitType(UnitTypeInfo._HEAVY_TANK);
        Unit enemy = definition.otherUnit;
        chubbyCharles.setMp(4);
        game.withUnit(chubbyCharles);
        Cell[][] cells = definition.cells;
        for (int row = 0; row < 4; row++) {
            for (int column = 0; column < 4; column++) {
                final Cell cell = new Cell(String.format("%d:%d", row, column));
                cell.setBiome(Biome.GRASS);
                cell.setPassable(true);
                cell.setX(column);
                cell.setY(row);
                cells[row][column] = cell;
                if (row > 0) {
                    cell.setTop(cells[row-1][column]);
                }
                if (column > 0) {
                    cell.setLeft(cells[row][column-1]);
                }
            }
        }
        Cell startCell = cells[0][1];
        chubbyCharles.setPosition(startCell);
        cells[1][0].setPassable(false);
        cells[1][0].setBiome(Biome.WATER);
        cells[1][1].setPassable(false);
        cells[1][1].setBiome(Biome.WATER);
        cells[1][2].setUnit(enemy);
        cells[1][2].setBiome(Biome.FOREST);

        game.withCells(
                Arrays.stream(cells)
                        .flatMap(Arrays::stream)
                        .toArray(Cell[]::new)
        );

        return definition;
    }

    /**
     Y -> player unit, 0 -> passable, X -> blocked, E -> enemy
     * Y E
     * O O
     */
    public static Definition sampleGameAttack() {
        int height = 2;
        int width = 2;
        Definition definition = new Definition(new Cell[height][width]);

        Game game = definition.game;
        Unit chubbyCharles = definition.playerUnit;
        definition.playerUnit
                .setUnitType(UnitTypeInfo._HEAVY_TANK)
                .setLeader(new Player("skynet"))
                .setCanAttack(Collections.singletonList(UnitTypeInfo._CHOPPER))
                .setAttackReady(true);
        Unit enemy = definition.otherUnit
                .setUnitType(UnitTypeInfo._CHOPPER)
                .setLeader(new Player("enemy"))
                .setGame(game);
        chubbyCharles.setMp(4);
        game.withUnit(chubbyCharles);
        Cell[][] cells = definition.cells;
        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                final Cell cell = new Cell(String.format("%d:%d", row, column));
                cell.setBiome(Biome.GRASS);
                cell.setPassable(true);
                cell.setX(column);
                cell.setY(row);
                cells[row][column] = cell;
                if (row > 0) {
                    cell.setTop(cells[row-1][column]);
                }
                if (column > 0) {
                    cell.setLeft(cells[row][column-1]);
                }
            }
        }

        Cell startCell = cells[0][0];
        chubbyCharles.setPosition(startCell);
        enemy.setPosition(cells[0][1]);

        cells[1][1].setPassable(false);
        cells[1][1].setBiome(Biome.WATER);
        cells[1][0].setPassable(false);
        cells[1][0].setBiome(Biome.MOUNTAIN);

        game.withCells(
                Arrays.stream(cells)
                        .flatMap(Arrays::stream)
                        .toArray(Cell[]::new)
        );

        return definition;
    }

    public static Definition skynetMoveTestGame(@NonNull final Player player,
                                                @NonNull final Unit testUnit) {
        final Definition definition = new Definition(new Cell[5][5]);

        final Game game = definition.game;
        player.setCurrentGame(game);
        final Player enemy = new Player("enemy").setCurrentGame(game);
        final Unit playerUnit = definition.playerUnit
                .setLeader(player)
                .setGame(game);
        testUnit.setLeader(player)
                .setGame(game);
        final Unit enemyUnit = definition.otherUnit.setLeader(enemy).setGame(game);

        final Cell[][] cells = definition.cells;
        for (int row = 0; row < 5; row++) {
            for (int column = 0; column < 5; column++) {
                final Cell cell = new Cell(String.format("%d:%d", row, column));
                cell.setBiome(Biome.GRASS);
                cell.setPassable(true);
                cell.setX(column);
                cell.setY(row);
                cells[row][column] = cell;
                if (row > 0) {
                    cell.setTop(cells[row-1][column]);
                }
                if (column > 0) {
                    cell.setLeft(cells[row][column-1]);
                }
            }
        }

        testUnit.setPosition(cells[0][0]);
        playerUnit.setPosition(cells[0][1]);
        enemyUnit.setPosition(cells[4][4]);

        game.withCells(
                Arrays.stream(cells)
                        .flatMap(Arrays::stream)
                        .toArray(Cell[]::new)
        );

        return definition;
    }

    public static class Definition {
        final public Game game = new Game("game");
        final public Unit playerUnit = new Unit("Helicopter Dick");
        final public Unit otherUnit = new Unit("enemy");
        final public Cell[][] cells;

        public Definition(Cell[][] cells) {
            this.cells = cells;
        }
    }
}
