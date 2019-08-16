package de.uniks.se19.team_g.project_rbsg.skynet.behaviour;

import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.*;
import de.uniks.se19.team_g.project_rbsg.ingame.model.*;
import de.uniks.se19.team_g.project_rbsg.skynet.action.*;
import org.junit.*;

import java.util.*;

import static de.uniks.se19.team_g.project_rbsg.ingame.battlefield.TestGameBuilder.*;
import static org.junit.Assert.*;

public class SurrenderBehaviourTest
{

    @Test
    public void NoUnitCanAttackByTypeTest() {
        final Player player = new Player("me");
        final Definition definition = skynetSurrenderCantAttackTestGame(player);
        final Game game = definition.game;

        final SurrenderBehaviour surrenderBehaviour = new SurrenderBehaviour();

        Optional<? extends Action> action = surrenderBehaviour.apply(game, player);

        assertFalse(action.isPresent());

        player.getUnits().remove(0);

        action = surrenderBehaviour.apply(game, player);

        assertTrue(action.isPresent());
    }

    @Test
    public void CantMoveUnitTest() {
        final Player player = new Player("me");
        final Unit unit = new Unit("VeryBadGuy");
        final Definition definition = skynetSurrenderCantMoveGame(player, unit);
        final Game game = definition.game;

        final SurrenderBehaviour surrenderBehaviour = new SurrenderBehaviour();

        Optional<? extends Action> action = surrenderBehaviour.apply(game, player);

        assertFalse(action.isPresent());

        unit.setPosition(definition.cells[2][3]);

        action = surrenderBehaviour.apply(game, player);

        assertTrue(action.isPresent());
    }
}
