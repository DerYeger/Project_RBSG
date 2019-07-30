package de.uniks.se19.team_g.project_rbsg.ingame.battlefield.uiModel;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Cell;
import org.junit.Test;

import static org.junit.Assert.*;

public class TileTest {

    @Test
    public void getHighlighting() {

        Cell cell = new Cell("1");

        Tile sut = new Tile(cell);
    }
}