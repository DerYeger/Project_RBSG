package de.uniks.se19.team_g.project_rbsg.ingame.model;

import javafx.beans.value.ChangeListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CellTest {

    @Mock
    ChangeListener<Boolean> changeListener;

    @Test
    public void selectedProperty() {

        Cell cellOne = new Cell("1");
        Cell cellTwo = new Cell("1");
        Game game = new Game("1");

        assertNull(game.getSelected());
        assertFalse(cellOne.isSelected());
        assertFalse(cellTwo.isSelected());

        game.setSelected(cellOne);

        assertSame(cellOne, game.getSelected());
        assertTrue(cellOne.isSelected());
        assertFalse(cellTwo.isSelected());

        game.setSelected(cellTwo);
        assertFalse(cellOne.isSelected());
        assertTrue(cellTwo.isSelected());

        game.setSelected(cellTwo);
        assertFalse(cellOne.isSelected());
        assertTrue(cellTwo.isSelected());

        game.clearSelection();
        assertFalse(cellOne.isSelected());
        assertFalse(cellTwo.isSelected());

        cellOne.selectedProperty().addListener(changeListener);
        verifyZeroInteractions(changeListener);
        game.setSelected(cellOne);
        verify(changeListener).changed(any(), eq(false), eq(true));
        game.setSelected(cellTwo);
        verify(changeListener).changed(any(), eq(true), eq(false));

    }
}