package de.uniks.se19.team_g.project_rbsg.ingame.event;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Cell;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;
import org.junit.Assert;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.Assert.*;

public class CommandBuilderTest extends ApplicationTest {

    @Test
    public void moveUnit() {

        Unit unit = new Unit("unit");
        Cell cell1 = new Cell("c1");
        Cell cell2 = new Cell("c2");
        Cell cell3 = new Cell("c3");

        ArrayList<Cell> path = new ArrayList<>(3);
        path.add(cell1);
        path.add(cell2);
        path.add(cell3);

        ArrayList<String> pathAsIds = new ArrayList<>(3);
        pathAsIds.add("c1");
        pathAsIds.add("c2");
        pathAsIds.add("c3");

        Map<String, Object> command = CommandBuilder.moveUnit(unit, path);

        Assert.assertSame( "moveUnit", command.get("action"));
        CommandBuilder.MoveUnitData data = (CommandBuilder.MoveUnitData) command.get("data");
        Assert.assertSame( "unit", data.unitId);
        Assert.assertEquals( pathAsIds, data.path);
    }
}