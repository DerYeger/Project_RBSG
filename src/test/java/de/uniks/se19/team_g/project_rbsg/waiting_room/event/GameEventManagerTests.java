package de.uniks.se19.team_g.project_rbsg.waiting_room.event;

import de.uniks.se19.team_g.project_rbsg.server.websocket.IWebSocketCallback;
import de.uniks.se19.team_g.project_rbsg.server.websocket.WebSocketClient;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Jan Müller
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        GameEventManagerTests.ContextConfiguration.class,
        GameEventManager.class
})
public class GameEventManagerTests {

    private static boolean socketStarted = false;
    private static boolean socketStopped = false;

    private final String gameId = "12345";

    @Autowired
    private GameEventManager gameEventManager;

    @TestConfiguration
    public static class ContextConfiguration {
        @Bean
        public WebSocketClient webSocketClient() {
            return new WebSocketClient() {
                @Override
                public void start(String endpoint, IWebSocketCallback webSocketCallback)
                {
                    if (endpoint.equals("/game?gameId=12345")) {
                        socketStarted = true;
                    }
                }

                @Override
                public void stop() {
                    socketStopped = true;
                }
            };
        }
    }

    private static class TestGameEventHandler implements GameEventHandler {
        public String handledMessage;

        @Override
        public void handle(@NonNull final String message) {
            handledMessage = message;
        }
    }

    @Test
    public void testStartSocket() {
        gameEventManager.startSocket(gameId);
        System.out.println(socketStarted);
        Assert.assertTrue(socketStarted);
    }

    @Test
    public void testTerminate() {
        gameEventManager.terminate();
        Assert.assertTrue(socketStopped);
    }

    @Test
    public void testAddHandler() {
        final TestGameEventHandler testGameEventHandler = new TestGameEventHandler();
        gameEventManager.addHandler(testGameEventHandler);
        Assert.assertTrue(gameEventManager.getHandlers().contains(testGameEventHandler));
    }

    @Test
    public void testHandle() {
        final TestGameEventHandler testGameEventHandler = new TestGameEventHandler();
        final String message = "A message!";
        gameEventManager
                .addHandler(testGameEventHandler)
                .handle(message);
        Assert.assertEquals(message, testGameEventHandler.handledMessage);
    }
}
