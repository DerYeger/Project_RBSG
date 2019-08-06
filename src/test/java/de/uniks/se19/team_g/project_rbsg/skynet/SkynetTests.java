package de.uniks.se19.team_g.project_rbsg.skynet;

import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.TestGameBuilder;
import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.Tour;
import de.uniks.se19.team_g.project_rbsg.ingame.event.IngameApi;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Game;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;
import de.uniks.se19.team_g.project_rbsg.skynet.action.ActionExecutor;
import de.uniks.se19.team_g.project_rbsg.skynet.action.MovementAction;
import de.uniks.se19.team_g.project_rbsg.skynet.behaviour.MovementBehaviour;
import org.junit.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class SkynetTests {

    @Test
    public void testSkynetTurn() {
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
                .setCurrentBehaviour(movementBehaviour);

        final MovementAction action = new MovementAction(unit, new Tour());

        when(movementBehaviour.apply(game, player))
                .thenReturn(Optional.of(action));

        skynet.turn();

        verify(api).move(action.unit, action.tour);
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
                .setCurrentBehaviour(movementBehaviour);

        final MovementAction action = new MovementAction(unit, new Tour());

        when(movementBehaviour.apply(game, player))
                .thenReturn(Optional.of(action));

        skynet.turn();

        verifyNoMoreInteractions(api);
    }
}
