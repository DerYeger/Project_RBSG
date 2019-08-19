package de.uniks.se19.team_g.project_rbsg.skynet;

import de.uniks.se19.team_g.project_rbsg.ingame.event.IngameApi;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Game;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import de.uniks.se19.team_g.project_rbsg.skynet.action.ActionExecutor;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
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

        final DoubleProperty testProperty = new SimpleDoubleProperty(0.5);

        skynet.getBot().frequency.bindBidirectional(testProperty);

        assertFalse(skynet.isBotRunning());

        testProperty.set(10);

        skynet.startBot();

        Thread botThread = skynet.getBotThread();
        assertThat(botThread, notNullValue());

        skynet.startBot();
        assertThat(skynet.getBotThread(), is(botThread));

        assertTrue(skynet.isBotRunning());

        skynet.stopBot();
        
        Thread.sleep(200);

        assertFalse(skynet.isBotRunning());

        skynet.startBot();
        assertThat(skynet.getBotThread(), not(botThread));
        skynet.stopBot();
    }

}
