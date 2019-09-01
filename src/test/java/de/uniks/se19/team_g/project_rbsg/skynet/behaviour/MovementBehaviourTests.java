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

        System.out.println();

        final Optional<MovementAction> action = movementBehaviour.apply(game, player);

        assertTrue(action.isPresent());

        final MovementAction movementAction = action.get();

        assertEquals(unit, movementAction.unit);
//        assertEquals(5, movementAction.tour.getCost());

        final Cell target = movementAction.tour.getTarget();
        System.out.println();
        System.out.println("Real");
        System.out.println(target);
        System.out.println(target.getDistance(definition.otherUnit.getPosition()));
        System.out.println();
        final Cell expectedCell = game.getCells().filtered(cell -> cell.getX() == 2 && cell.getY() == 0).get(0);
        System.out.println("Expected");
        System.out.println(expectedCell);
        System.out.println(expectedCell.getDistance(definition.otherUnit.getPosition()));
//        assertEquals(0, target.getY());
//        assertEquals(2, target.getX());
//        assertEquals(6, target.getDistance(definition.otherUnit.getPosition()));
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
