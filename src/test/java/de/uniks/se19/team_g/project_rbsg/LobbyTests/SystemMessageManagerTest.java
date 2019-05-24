package de.uniks.se19.team_g.project_rbsg.LobbyTests;

import de.uniks.se19.team_g.project_rbsg.JavaConfig;
import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.Contract.ISystemMessageHandler;
import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.Contract.IWebSocketCallback;
import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.SystemMessageManager;
import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.WebSocketClient;
import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.WebSocketConfigurator;
import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.WebSocketFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.websocket.Session;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        JavaConfig.class,
        SystemMessageManagerTest.ContextConfiguration.class,
        SystemMessageManager.class
})
public class SystemMessageManagerTest
{
    private ArrayList<String> messageList = new ArrayList<>();

    @Autowired
    private ApplicationContext context;

    @TestConfiguration
    public static class ContextConfiguration {
        @Bean
        public WebSocketFactory webSocketFactory() {
            return new WebSocketFactory() {
                @Override
                public WebSocketClient getSocket(String endpoint, IWebSocketCallback webSocketCallback) {
                    return new WebSocketClient(endpoint, webSocketCallback) {
                        @Override
                        public void start()
                        {
                            try
                            {
                                onMessage("", null);
                            }
                            catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onMessage(final String message, final Session session) throws IOException {
                            webSocketCallback.handle("Hallo du da im Radio!");
                        }
                    };
                }
            };
        }
    }

    @Test
    public void messageArrived() throws IOException
    {
        SystemMessageManager systemMessageManager = context.getBean(SystemMessageManager.class);
        systemMessageManager.addMessageHandler(new TestMessageHandler());
        systemMessageManager.getWebSocketClient().onMessage("", null);
        assertEquals(1, messageList.size());
        assertEquals("Hallo du da im Radio!", messageList.get(0));

        messageList.clear();
    }

    @Test
    public void startWebSocket() {
        SystemMessageManager systemMessageManager = context.getBean(SystemMessageManager.class);
        systemMessageManager.addMessageHandler(new TestMessageHandler());
        systemMessageManager.startSocket();
        assertEquals(0, messageList.size());

        WebSocketConfigurator.userKey = "aUserKey";
        systemMessageManager.startSocket();
        assertEquals(1 , messageList.size());
        assertEquals("Hallo du da im Radio!", messageList.get(0));

        messageList.clear();

//        systemMessageManager.setWebSocketClient(null);
//        systemMessageManager.startSocket();
//        assertEquals(1 , messageList.size());
    }

    public class TestMessageHandler implements ISystemMessageHandler {

        @Override
        public void handleSystemMessage(String message)
        {
            messageList.add(message);
        }
    }
}
