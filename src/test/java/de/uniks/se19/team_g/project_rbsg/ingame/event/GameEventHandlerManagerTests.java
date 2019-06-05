package de.uniks.se19.team_g.project_rbsg.ingame.event;

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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        GameEventHandlerManagerTests.ContextConfiguration.class,
        GameEventHandlerManager.class
})
public class GameEventHandlerManagerTests {

    private static boolean socketStarted = false;
    private static boolean socketStopped = false;

    private final int gameId = 12345;

    @Autowired
    private GameEventHandlerManager gameEventHandlerManager;

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
        gameEventHandlerManager.startSocket(gameId);
        System.out.println(socketStarted);
        Assert.assertTrue(socketStarted);
    }

    @Test
    public void testTerminate() {
        gameEventHandlerManager.terminate();
        Assert.assertTrue(socketStopped);
    }

    @Test
    public void testAddHandler() {
        final TestGameEventHandler testGameEventHandler = new TestGameEventHandler();
        gameEventHandlerManager.addHandler(testGameEventHandler);
        Assert.assertTrue(gameEventHandlerManager.getHandlers().contains(testGameEventHandler));
    }

    @Test
    public void testHandle() {
        final TestGameEventHandler testGameEventHandler = new TestGameEventHandler();
        final String message = "A message!";
        gameEventHandlerManager
                .addHandler(testGameEventHandler)
                .handle(message);
        Assert.assertEquals(message, testGameEventHandler.handledMessage);
    }
}
