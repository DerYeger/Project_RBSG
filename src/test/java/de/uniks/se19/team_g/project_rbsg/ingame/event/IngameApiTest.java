package de.uniks.se19.team_g.project_rbsg.ingame.event;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.testfx.framework.junit.ApplicationTest;

import static org.junit.Assert.*;

public class IngameApiTest extends ApplicationTest {

    @Test
    public void attack() {
        GameEventManager gameEventManager = Mockito.mock(GameEventManager.class);

        Unit attacker = new Unit("1");
        Unit target = new Unit("2");
        IngameApi.AttackCommand command = new IngameApi.AttackCommand();

        IngameApi sut = Mockito.mock(IngameApi.class, InvocationOnMock::callRealMethod);
        sut.setGameEventManager(gameEventManager);

        Mockito.doReturn(command).when(sut).buildAttack(attacker, target);

        sut.attack(attacker, target);

        Mockito.verify(gameEventManager).sendMessage(command);
    }

    @Test
    public void buildAttack() {
        IngameApi sut = new IngameApi();

        Unit u1 = new Unit("1");
        Unit u2 = new Unit("2");
        IngameApi.AttackCommand attackCommand = sut.buildAttack(u1, u2);

        Assert.assertEquals("attackUnit", attackCommand.action);
        Assert.assertEquals( "1", attackCommand.data.unitId);
        Assert.assertEquals( "2", attackCommand.data.toAttackId);
    }
}