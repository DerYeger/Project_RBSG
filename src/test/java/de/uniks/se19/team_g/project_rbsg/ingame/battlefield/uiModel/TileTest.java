package de.uniks.se19.team_g.project_rbsg.ingame.battlefield.uiModel;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Cell;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Game;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;
import org.junit.Test;

import static org.junit.Assert.assertSame;

public class TileTest {

    @Test
    public void getHighlightingTwo() {

        Game game = new Game("1");
        Player player = new Player("columnPlayer");
        player.setIsPlayer(true);
        Player other = new Player("other");
        Cell cell = new Cell("1");
        Unit unitOne = new Unit("1");
        Unit unitTwo = new Unit("2");
        unitOne.setLeader(player);
        unitTwo.setLeader(other);
        cell.setUnit(unitOne);

        game.withCells(cell);

        Tile sut = new Tile(cell);

        assertSame(HighlightingTwo.NONE, sut.getHighlightingTwo());

        cell.setSelectedIn(game);
        assertSame(HighlightingTwo.SELECTED, sut.getHighlightingTwo());

        new Cell("2").setSelectedIn(game);

        assertSame(HighlightingTwo.NONE, sut.getHighlightingTwo());

        cell.setHoveredIn(game);

        assertSame(HighlightingTwo.HOVERED, sut.getHighlightingTwo());

        unitTwo.setHoveredIn(game);
        assertSame(HighlightingTwo.NONE, sut.getHighlightingTwo());

        unitOne.setSelectedIn(game);
        assertSame(HighlightingTwo.SELECETD_WITH_UNITS, sut.getHighlightingTwo());

        unitOne.setPosition(null);
        assertSame(HighlightingTwo.NONE, sut.getHighlightingTwo());

        unitTwo.setSelectedIn(game);
        assertSame(HighlightingTwo.NONE, sut.getHighlightingTwo());

        cell.setUnit(unitTwo);
        assertSame(HighlightingTwo.SELECTED, sut.getHighlightingTwo());
    }
}