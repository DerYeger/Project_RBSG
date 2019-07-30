package de.uniks.se19.team_g.project_rbsg.ingame.battlefield.uiModel;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Cell;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Game;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;

public class TileTest {

    @Test
    public void getHighlightingTwo() {

        Game game = new Game("1");
        Player player = new Player("player");
        player.setIsPlayer(true);
        Player other = new Player("other");
        Cell cell = new Cell("1");
        Unit unitOne = new Unit("1");
        Unit unitTwo = new Unit("2");
        unitOne.setLeader(player);
        unitTwo.setLeader(other);
        cell.setUnit(unitOne);

        Tile sut = new Tile(cell);

        assertSame(HighlightingTwo.NONE, sut.getHighlightingTwo());

        game.setSelected(cell);
        assertSame(HighlightingTwo.SELECTED, sut.getHighlightingTwo());

        game.setSelected(new Cell("2"));

        assertSame(HighlightingTwo.NONE, sut.getHighlightingTwo());

        game.setHovered(cell);

        assertSame(HighlightingTwo.HOVERED, sut.getHighlightingTwo());

        game.setHovered(unitTwo);
        assertSame(HighlightingTwo.NONE, sut.getHighlightingTwo());

        game.setSelected(unitOne);
        assertSame(HighlightingTwo.SELECETD_WITH_UNITS, sut.getHighlightingTwo());

        cell.setUnit(null);
        assertSame(HighlightingTwo.NONE, sut.getHighlightingTwo());

        game.setSelected(unitTwo);
        assertSame(HighlightingTwo.NONE, sut.getHighlightingTwo());

        cell.setUnit(unitTwo);
        assertSame(HighlightingTwo.SELECTED, sut.getHighlightingTwo());
    }
}