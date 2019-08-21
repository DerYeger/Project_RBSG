package de.uniks.se19.team_g.project_rbsg.ingame.state;

import de.uniks.se19.team_g.project_rbsg.ingame.model.*;
import de.uniks.se19.team_g.project_rbsg.ingame.model.util.PlayerUtil;
import de.uniks.se19.team_g.project_rbsg.ingame.model.util.UnitUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DefaultRemoveListenerTest {


    private List<RemoveAction> actions;
    private Unit unit;
    private Player player;
    private Game game;
    private DefaultRemoveListener sut;
    private GameEventDispatcher dispatcher;
    private ModelManager modelManager;
    private Cell cell;

    @Before
    public void setup() {
        sut = new DefaultRemoveListener();

        game = new Game("game");
        player = new Player("player");
        unit = new Unit("unit");
        cell = new Cell("cell");

        actions = new ArrayList<>();

        dispatcher = mock(GameEventDispatcher.class);
        modelManager = mock(ModelManager.class);
        when(dispatcher.getModelManager()).thenReturn(modelManager);
        when(modelManager.getEntityById(game.getId())).thenReturn(game);
        when(modelManager.getEntityById(player.getId())).thenReturn(player);
        when(modelManager.getEntityById(unit.getId())).thenReturn(unit);
        when(modelManager.getEntityById(cell.getId())).thenReturn(cell);
        Mockito.doAnswer(invocation -> {
            actions.add(invocation.getArgument(0));
            return null;
        }).when(modelManager).addAction(any(RemoveAction.class));
    }

    protected void accept(GameRemoveObjectEvent unitFromCellRemoved) {
        sut.accept(unitFromCellRemoved, dispatcher);
    }

    @Test
    public void unitFromCellRemoved() {
        GameRemoveObjectEvent unitFromCellRemoved = new GameRemoveObjectEvent(
                "unit",
                null,
                "cell",
                Cell.UNIT
        );
        accept(unitFromCellRemoved);
        RemoveAction action = actions.get(0);
        Assert.assertNotNull(action);

        action.undo();
        Assert.assertSame(unit, cell.getUnit());
        Assert.assertSame(cell, unit.getPosition());
        action.run();
        Assert.assertNull(cell.getUnit());
        Assert.assertNull(unit.getPosition());
    }

    @Test
    public void unitFromGameRemoved() {
        GameRemoveObjectEvent unitFromGameRemoved = new GameRemoveObjectEvent(
                "unit",
                null,
                "game",
                Game.UNITS
        );
        accept(unitFromGameRemoved);
        RemoveAction action = actions.get(0);
        Assert.assertNotNull(action);

        action.undo();
        Assert.assertTrue(game.getUnits().contains(unit));
        Assert.assertSame(game, unit.getGame());
        action.run();
        Assert.assertFalse(game.getUnits().contains(unit));
        Assert.assertNull(unit.getGame());
    }

    @Test
    public void unitFromPlayerRemoved() {

        GameRemoveObjectEvent unitFromPlayerRemoved = new GameRemoveObjectEvent(
                "unit",
                null,
                "player",
                Player.UNITS
        );
        accept(unitFromPlayerRemoved);
        RemoveAction action = actions.get(0);
        Assert.assertNotNull(action);

        action.undo();
        Assert.assertTrue(player.getUnits().contains(unit));
        Assert.assertSame(player, unit.getLeader());
        action.run();
        Assert.assertFalse(player.getUnits().contains(unit));
        Assert.assertNull(unit.getLeader());
    }

    @Test
    public void playerFromGameRemoved() {

        GameRemoveObjectEvent playerFromGameRemoved = new GameRemoveObjectEvent(
                "player",
                null,
                "game",
                Game.PLAYERS
        );
        accept(playerFromGameRemoved);
        RemoveAction action = actions.get(0);
        Assert.assertNotNull(action);

        action.undo();
        Assert.assertTrue(game.getPlayers().contains(player));
        Assert.assertSame(game, player.getCurrentGame());
        action.run();
        Assert.assertFalse(game.getPlayers().contains(player));
        Assert.assertNull(player.getCurrentGame());
    }
}