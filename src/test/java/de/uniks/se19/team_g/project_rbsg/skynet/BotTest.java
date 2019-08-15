package de.uniks.se19.team_g.project_rbsg.skynet;

import de.uniks.se19.team_g.project_rbsg.ingame.event.IngameApi;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Game;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import de.uniks.se19.team_g.project_rbsg.skynet.action.ActionExecutor;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class BotTest extends ApplicationTest
{
    @Test
    public void testBot()  throws InterruptedException
    {
        Skynet skynet = new Skynet(new ActionExecutor(new IngameApi()), new Game("2"), new Player("2"))
        {
            @Override
            public Skynet turn()
            {
                return this;
            }
        };

        assertFalse(skynet.isBotRunning());

        skynet.startBot();

        Thread botThread = skynet.getBotThread();
        assertThat(botThread, notNullValue());

        skynet.startBot();
        assertThat(skynet.getBotThread(), is(botThread));

        assertTrue(skynet.isBotRunning());

        skynet.stopBot();
        
        Thread.sleep(800);

        assertFalse(skynet.isBotRunning());

        skynet.startBot();
        assertThat(skynet.getBotThread(), not(botThread));
        skynet.stopBot();
    }

}
