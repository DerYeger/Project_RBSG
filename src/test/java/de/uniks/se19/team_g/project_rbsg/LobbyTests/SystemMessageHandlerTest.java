package de.uniks.se19.team_g.project_rbsg.LobbyTests;

import de.uniks.se19.team_g.project_rbsg.JavaConfig;
import de.uniks.se19.team_g.project_rbsg.Lobby.CrossCutting.DataClasses.Lobby;
import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.SystemMessageHandler.DefaultSystemMessageHandler;
import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.SystemMessageHandler.UserJoinedMessageHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testfx.framework.junit.ApplicationTest;

import static org.junit.Assert.*;

/**
 * @author Georg Siebert
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        JavaConfig.class,
        DefaultSystemMessageHandler.class,
        UserJoinedMessageHandler.class
})
public class SystemMessageHandlerTest extends ApplicationTest
{
    private final String userJoinedMessage = "{\"action\":\"userJoined\",\"data\":{\"name\":\"hello2\"}}";

    @Autowired
    private ApplicationContext context;

    @Test
    public void testDefaultSystemMessageHandler() {
        DefaultSystemMessageHandler defaultHandler = context.getBean(DefaultSystemMessageHandler.class);

        defaultHandler.handleSystemMessage("Hallo");
    }

    @Test
    public void testUserJoinedMessageHandler() {
        UserJoinedMessageHandler userJoinedMessageHandler = context.getBean(UserJoinedMessageHandler.class);

        Lobby lobby = new Lobby();
        userJoinedMessageHandler.setLobby(lobby);

        assertNotNull(lobby.getPlayers());

        userJoinedMessageHandler.handleSystemMessage(userJoinedMessage);

        sleep(500);

        assertEquals(1, lobby.getPlayers().size());
        assertEquals("hello2" , lobby.getPlayers().get(0).getName());
    }
}
