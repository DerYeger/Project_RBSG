package de.uniks.se19.team_g.project_rbsg.ingame.battlefield;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Cell;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Game;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MovementEvaluatorTest {

    @Test
    public void test() {

        Game game = new Game("tolles game");
        Unit helicopterDick = new Unit("helicopterDick");
        helicopterDick.setMp(4);
        game.withUnit(helicopterDick);

        /*
            Y -> player, 0 -> passable, X -> blocked
            OYOO
            XXOO
            -OOO
            --O-
         */
        Cell[][] cells = new Cell[4][4];
        for (int row = 0; row < 4; row++) {
            for (int column = 0; column < 4; column++) {
                final Cell cell = new Cell(String.format("%d:%d", row, column));
                cell.setPassable(true);
                cell.setX(row);
                cell.setY(column);
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
        helicopterDick.setPosition(startCell);
        cells[1][0].setPassable(false);
        cells[1][1].setPassable(false);

        game.withCells(
                Arrays.stream(cells)
                    .flatMap(Arrays::stream)
                        .toArray(Cell[]::new)
        );

        MovementEvaluator sut = new MovementEvaluator();

        Map<Cell, Tour> tours = sut.getAllowedTours(helicopterDick);

        // check one step way
        final Tour tourTo0_0 = tours.get(cells[0][0]);
        Assert.assertSame(cells[0][0], tourTo0_0.getTarget());
        Assert.assertEquals( 1L, tourTo0_0.getPath().size());
        Assert.assertSame( tourTo0_0.getTarget(), tourTo0_0.getPath().get(0));
        Assert.assertEquals(1, tourTo0_0.getCost());

        // check starting cell in two steps
        final Tour circle = tours.get(startCell);
        Assert.assertEquals(2, circle.getPath().size());
        Assert.assertEquals( startCell, circle.getPath().get(1));
        Assert.assertNotEquals( startCell, circle.getPath().get(0));

        // way of length 2 and 4 possible, should choose 2
        final Tour tourTo0_3 = tours.get(cells[0][3]);
        Assert.assertSame(cells[0][3], tourTo0_3.getTarget());

        final List<Cell> pathTo0_3 = tourTo0_3.getPath();
        Assert.assertEquals( 2L, pathTo0_3.size());
        Assert.assertSame( cells[0][2], pathTo0_3.get(0));
        Assert.assertSame( tourTo0_3.getTarget(), tourTo0_3.getPath().get(1));

        // do not move through obstacles
        final Tour tourTo2_1 = tours.get(cells[2][1]);
        Assert.assertSame(cells[2][1], tourTo2_1.getTarget());

        final List<Cell> pathTo2_1 = tourTo2_1.getPath();
        Assert.assertEquals( 4L, pathTo2_1.size());
        Assert.assertSame( cells[0][2], pathTo2_1.get(0));
        Assert.assertSame( cells[1][2], pathTo2_1.get(1));
        Assert.assertSame( cells[2][2], pathTo2_1.get(2));
        Assert.assertSame( tourTo2_1.getTarget(), pathTo2_1.get(3));

        // check other
        Assert.assertEquals(10L, tours.size());
        Assert.assertFalse(tours.containsKey(cells[1][1]));
        Assert.assertFalse(tours.containsKey(cells[2][0]));

    }
}
