package de.uniks.se19.team_g.project_rbsg.ingame.battlefield;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Cell;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class MovementEvaluatorTest {

    @Test
    public void test() {

        TestGameBuilder.Definition definition = TestGameBuilder.sampleGameAlpha();

        Cell[][] cells = definition.cells;
        Unit helicopterDick = definition.playerUnit;
        Cell startCell = helicopterDick.getPosition();

        MovementEvaluator sut = new MovementEvaluator();

        Map<Cell, Tour> tours = sut.getAllowedTours(helicopterDick);

        // check one step way
        final Tour tourTo0_0 = tours.get(cells[0][0]);
        Assert.assertSame(cells[0][0], tourTo0_0.getTarget());
        Assert.assertEquals( 1L, tourTo0_0.getPath().size());
        Assert.assertSame( tourTo0_0.getTarget(), tourTo0_0.getPath().get(0));
        Assert.assertEquals(1, tourTo0_0.getCost());

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
        Assert.assertEquals(9L, tours.size());
        Assert.assertFalse(tours.containsKey(cells[1][1]));
        Assert.assertFalse(tours.containsKey(cells[2][0]));

    }
}
