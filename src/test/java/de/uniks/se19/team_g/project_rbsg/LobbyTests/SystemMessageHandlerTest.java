package de.uniks.se19.team_g.project_rbsg.LobbyTests;

import de.uniks.se19.team_g.project_rbsg.JavaConfig;
import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.SystemMessageHandler.DefaultSystemMessageHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        JavaConfig.class,
        DefaultSystemMessageHandler.class
})
public class SystemMessageHandlerTest
{
    @Autowired
    private ApplicationContext context;

    @Test
    public void testDefaultSystemMessageHandler() {
        DefaultSystemMessageHandler defaultHandler = context.getBean(DefaultSystemMessageHandler.class);
        defaultHandler.handleSystemMessage("Hallo");
    }

}
