package de.uniks.se19.team_g.project_rbsg.skynet.behaviour;

import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.TestGameBuilder;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Cell;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Game;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;
import de.uniks.se19.team_g.project_rbsg.skynet.action.MovementAction;
import de.uniks.se19.team_g.project_rbsg.skynet.behaviour.movement.MovementBehaviour;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class MovementBehaviourTests {

    @Test
    public void testMovementBehaviour() {
        final Player player = new Player("skynet");
        final Unit unit = new Unit("testUnit")
                .setRemainingMovePoints(5);
        final TestGameBuilder.Definition definition = TestGameBuilder.skynetMoveTestGame(player, unit);
        final Game game = definition.game;

        final MovementBehaviour movementBehaviour = new MovementBehaviour();

        final Optional<MovementAction> action = movementBehaviour.apply(game, player);

        assertTrue(action.isPresent());

        final MovementAction movementAction = action.get();

        assertEquals(unit, movementAction.unit);
        assertEquals(5, movementAction.tour.getCost());

        final Cell target = movementAction.tour.getTarget();
        assertEquals(3, target.getDistance(definition.otherUnit.getPosition()));
    }

    @Test
    public void testDijkstra() {
        final Player player = new Player("skynet");
        final Unit unit = new Unit("testUnit")
                .setRemainingMovePoints(5);
        final TestGameBuilder.Definition definition = TestGameBuilder.dijkstraTestGame(player, unit);
        final Game game = definition.game;

        final MovementBehaviour movementBehaviour = new MovementBehaviour();

        final Optional<MovementAction> action = movementBehaviour.apply(game, player);

        assertTrue(action.isPresent());

        final MovementAction movementAction = action.get();

        assertEquals(unit, movementAction.unit);
        assertEquals(5, movementAction.tour.getCost());

        final Cell target = movementAction.tour.getTarget();
        assertEquals(0, target.getY());
        assertEquals(2, target.getX());
        assertEquals(6, target.getDistance(definition.otherUnit.getPosition()));
    }

    @Test
    public void testMovementBehaviourException() {
        final Player player = new Player("skynet");
        final Unit unit = new Unit("testUnit")
                .setRemainingMovePoints(0);
        final TestGameBuilder.Definition definition = TestGameBuilder.skynetMoveTestGame(player, unit);
        final Game game = definition.game;

        final MovementBehaviour movementBehaviour = new MovementBehaviour();

        final Optional<MovementAction> action = movementBehaviour.apply(game, player);

        assertFalse(action.isPresent());
    }
}
