package de.uniks.se19.team_g.project_rbsg.skynet.behaviour;

import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.TestGameBuilder;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Game;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;
import de.uniks.se19.team_g.project_rbsg.skynet.action.AttackAction;
import de.uniks.se19.team_g.project_rbsg.skynet.behaviour.attack.AttackBehaviour;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class AttackBehaviourTests {

    @Test
    public void testAttackBehaviour() {
        final TestGameBuilder.Definition definition = TestGameBuilder.sampleGameAttack();
        final Game game = definition.game;
        final Unit unit = definition.playerUnit;
        final Player player = unit.getLeader();

        final AttackBehaviour attackBehaviour = new AttackBehaviour();

        final Optional<AttackAction> action = attackBehaviour.apply(game, player);

        assertTrue(action.isPresent());
        assertTrue(action.get() instanceof AttackAction);

        final AttackAction attackAction = (AttackAction) action.get();

        assertEquals(unit, attackAction.unit);
        assertEquals(definition.otherUnit, attackAction.target);
    }

    @Test
    public void testAttackBehaviourException() {
        final Player player = new Player("skynet");
        final Unit unit = new Unit("testUnit")
                .setRemainingMovePoints(0);
        final TestGameBuilder.Definition definition = TestGameBuilder.skynetMoveTestGame(player, unit);
        final Game game = definition.game;

        final AttackBehaviour attackBehaviour = new AttackBehaviour();

        final Optional<AttackAction> action = attackBehaviour.apply(game, player);

        assertFalse(action.isPresent());
    }
}
