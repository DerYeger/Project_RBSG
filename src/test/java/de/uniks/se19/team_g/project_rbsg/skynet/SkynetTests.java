package de.uniks.se19.team_g.project_rbsg.skynet;

import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.TestGameBuilder;
import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.Tour;
import de.uniks.se19.team_g.project_rbsg.ingame.event.IngameApi;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Game;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;
import de.uniks.se19.team_g.project_rbsg.skynet.action.ActionExecutor;
import de.uniks.se19.team_g.project_rbsg.skynet.action.AttackAction;
import de.uniks.se19.team_g.project_rbsg.skynet.action.MovementAction;
import de.uniks.se19.team_g.project_rbsg.skynet.action.PassAction;
import de.uniks.se19.team_g.project_rbsg.skynet.behaviour.AttackBehaviour;
import de.uniks.se19.team_g.project_rbsg.skynet.behaviour.MovementBehaviour;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SkynetTests {

    @Test
    public void testSkynetMoveTurn() {
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
    public void testSkynetAttackTurn() {
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
    public void testSkynetFallbackTurn() {
        final Player player = new Player("skynet");
        final Unit unit = new Unit("testUnit")
                .setRemainingMovePoints(0);
        final TestGameBuilder.Definition definition = TestGameBuilder.skynetMoveTestGame(player, unit);
        final Game game = definition.game
                .setCurrentPlayer(player)
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

        verify(api).endPhase();
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
}
