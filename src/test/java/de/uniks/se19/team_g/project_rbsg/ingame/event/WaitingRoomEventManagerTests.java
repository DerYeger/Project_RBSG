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

/**
 * @author Jan Müller
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        WaitingRoomEventManagerTests.ContextConfiguration.class,
        WaitingRoomEventManager.class
})
public class WaitingRoomEventManagerTests {

    private static boolean socketStarted = false;
    private static boolean socketStopped = false;

    private final String gameId = "12345";
    private final String armyID = "54321";

    @Autowired
    private WaitingRoomEventManager waitingRoomEventManager;

    @TestConfiguration
    public static class ContextConfiguration {
        @Bean
        public WebSocketClient webSocketClient() {
            return new WebSocketClient() {
                @Override
                public void start(String endpoint, IWebSocketCallback webSocketCallback)
                {
                    if (endpoint.equals("/game?gameId=12345&armyId=54321")) {
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

    private static class TestWaitingRoomEventHandler implements WaitingRoomEventHandler {
        public String handledMessage;

        @Override
        public void handle(@NonNull final String message) {
            handledMessage = message;
        }
    }

    @Test
    public void testStartSocket() {
        waitingRoomEventManager.startSocket(gameId, armyID);
        Assert.assertTrue(socketStarted);
    }

    @Test
    public void testTerminate() {
        waitingRoomEventManager.terminate();
        Assert.assertTrue(socketStopped);
    }

    @Test
    public void testAddHandler() {
        final TestWaitingRoomEventHandler testGameEventHandler = new TestWaitingRoomEventHandler();
        waitingRoomEventManager.addHandler(testGameEventHandler);
        Assert.assertTrue(waitingRoomEventManager.getHandlers().contains(testGameEventHandler));
    }

    @Test
    public void testHandle() {
        final TestWaitingRoomEventHandler testGameEventHandler = new TestWaitingRoomEventHandler();
        final String message = "A message!";
        waitingRoomEventManager
                .addHandler(testGameEventHandler)
                .handle(message);
        Assert.assertEquals(message, testGameEventHandler.handledMessage);
    }
}
