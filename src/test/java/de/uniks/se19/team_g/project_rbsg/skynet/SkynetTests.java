package de.uniks.se19.team_g.project_rbsg.skynet;

import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.*;
import de.uniks.se19.team_g.project_rbsg.ingame.event.IngameApi;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Game;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;
import de.uniks.se19.team_g.project_rbsg.skynet.action.ActionExecutor;
import de.uniks.se19.team_g.project_rbsg.skynet.action.AttackAction;
import de.uniks.se19.team_g.project_rbsg.skynet.action.MovementAction;
import de.uniks.se19.team_g.project_rbsg.skynet.action.SurrenderAction;
import de.uniks.se19.team_g.project_rbsg.skynet.behaviour.SurrenderBehaviour;
import de.uniks.se19.team_g.project_rbsg.skynet.behaviour.attack.AttackBehaviour;
import de.uniks.se19.team_g.project_rbsg.skynet.behaviour.movement.MovementBehaviour;

import org.junit.Test;

import java.util.Optional;

import static de.uniks.se19.team_g.project_rbsg.ingame.battlefield.TestGameBuilder.skynetSurrenderCantMoveGame;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SkynetTests {

    @Test
    public void testSkynetMove() {
        final Player player = new Player("skynet");
        final Unit unit = new Unit("testUnit")
                .setRemainingMovePoints(5);
        final TestGameBuilder.Definition definition = TestGameBuilder.skynetMoveTestGame(player, unit);
        final Game game = definition.game
                .setCurrentPlayer(player)
                .setPhase("movePhase");

        final IngameApi api = mock(IngameApi.class);
        final ActionExecutor actionExecutor = new ActionExecutor(api);
        final MovementBehaviour movementBehaviour = mock(MovementBehaviour.class);
        final Skynet skynet = new Skynet(actionExecutor, game, player)
                .addBehaviour(movementBehaviour, "movePhase");

        final MovementAction action = new MovementAction(unit, new Tour());

        when(movementBehaviour.apply(game, player))
                .thenReturn(Optional.of(action));

        skynet.turn();

        verify(api).move(action.unit, action.tour);
    }

    @Test
    public void testSkynetAttack() {
        final TestGameBuilder.Definition definition = TestGameBuilder.sampleGameAttack();

        final Unit unit = definition.playerUnit;
        final Player player = unit.getLeader();
        final Game game = definition.game
                .setCurrentPlayer(player)
                .setPhase("attackPhase");

        final IngameApi api = mock(IngameApi.class);
        final ActionExecutor actionExecutor = new ActionExecutor(api);
        final AttackBehaviour attackBehaviour = mock(AttackBehaviour.class);
        final Skynet skynet = new Skynet(actionExecutor, game, player)
                .addBehaviour(attackBehaviour, "attackPhase");

        final AttackAction action = new AttackAction(unit, definition.otherUnit);

        when(attackBehaviour.apply(game, player))
                .thenReturn(Optional.of(action));

        skynet.turn();

        verify(api).attack(action.unit, action.target);
    }

    @Test
    public void testSkynetFallbackMove() {
        testSkynetFallbackWithParams(1, false);
    }

    @Test
    public void testSkynetFallbackPass() {
        testSkynetFallbackWithParams(0, true);
    }

    private void testSkynetFallbackWithParams(final int remainingMovePoints, final boolean initiallyMoved) {
        final Player player = new Player("skynet");
        final Unit unit = new Unit("testUnit")
                .setRemainingMovePoints(remainingMovePoints);
        final TestGameBuilder.Definition definition = TestGameBuilder.skynetMoveTestGame(player, unit);
        final Game game = definition.game
                .setCurrentPlayer(player)
                .setInitiallyMoved(initiallyMoved)
                .setPhase("movePhase");

        game.setSelectedUnit(unit);

        final IngameApi api = mock(IngameApi.class);
        final ActionExecutor actionExecutor = new ActionExecutor(api);
        final MovementBehaviour movementBehaviour = mock(MovementBehaviour.class);
        final Skynet skynet = new Skynet(actionExecutor, game, player)
                .addBehaviour(movementBehaviour, "movePhase");

        when(movementBehaviour.apply(game, player))
                .thenReturn(Optional.empty());

        skynet.turn();

        assertNull(game.getSelected());
        assertFalse(unit.getNeighbors().isEmpty());

        verify(api).endPhase();
    }

    @Test
    public void testSkynetFallbackFailure() {
        final Player player = new Player("skynet");
        final Unit unit = new Unit("testUnit")
                .setRemainingMovePoints(0);
        final TestGameBuilder.Definition definition = TestGameBuilder.skynetMoveTestGame(player, unit);
        final Game game = definition.game
                .setCurrentPlayer(player)
                .setInitiallyMoved(false)
                .setPhase("movePhase");

        game.setSelectedUnit(unit);

        final ActionExecutor actionExecutor = mock(ActionExecutor.class);
        final MovementBehaviour movementBehaviour = mock(MovementBehaviour.class);
        final Skynet skynet = new Skynet(actionExecutor, game, player)
                .addBehaviour(movementBehaviour, "movePhase");

        when(movementBehaviour.apply(game, player))
                .thenReturn(Optional.empty());

        skynet.turn();

        verifyNoMoreInteractions(actionExecutor);
    }

    @Test
    public void testSkynetInvalidTurn() {
        final Player player = new Player("skynet");
        final Unit unit = new Unit("testUnit")
                .setRemainingMovePoints(5);
        final TestGameBuilder.Definition definition = TestGameBuilder.skynetMoveTestGame(player, unit);
        final Game game = definition.game
                .setPhase("movePhase");
        game.setCurrentPlayer(game.getPlayers().get(1));

        final IngameApi api = mock(IngameApi.class);
        final ActionExecutor actionExecutor = new ActionExecutor(api);
        final MovementBehaviour movementBehaviour = mock(MovementBehaviour.class);
        final Skynet skynet = new Skynet(actionExecutor, game, player)
                .addBehaviour(movementBehaviour, "movePhase");

        final MovementAction action = new MovementAction(unit, new Tour());

        when(movementBehaviour.apply(game, player))
                .thenReturn(Optional.of(action));

        skynet.turn();

        verifyNoMoreInteractions(api);
    }

    @Test
    public void testSkynetSurrenderAction() {
        final Player player = new Player("me");
        final Unit unit = new Unit("VeryBadGuy");
        final TestGameBuilder.Definition definition = skynetSurrenderCantMoveGame(player, unit);
        final Game game = definition.game;

        game.setCurrentPlayer(player);
        unit.setPosition(definition.cells[3][3]);

        final IngameApi api = mock(IngameApi.class);
        final ActionExecutor actionExecutor = new ActionExecutor(api);

        final SurrenderBehaviour surrenderBehaviour = mock(SurrenderBehaviour.class);
        final Skynet skynet = new Skynet(actionExecutor, game, player);
        skynet.addBehaviour(surrenderBehaviour, "surrender");

        final SurrenderAction surrenderAction = new SurrenderAction();

        when(surrenderBehaviour.apply(game, player)).thenReturn(Optional.of(surrenderAction));

        skynet.turn();

        verify(api).leaveGame();

        final BattleFieldController battleFieldController = mock(BattleFieldController.class);
        actionExecutor.setSurrenderGameAction(battleFieldController::surrender);

        skynet.turn();

        verify(battleFieldController).surrender();
    }
}
