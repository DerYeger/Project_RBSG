package de.uniks.se19.team_g.project_rbsg.waiting_room.event;

import com.fasterxml.jackson.databind.node.ObjectNode;
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
    private final String armyID = "54321";

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
                    if (endpoint.equals("/game?gameId=12345&armyId=54321")) {
                        socketStarted = true;
                    }
                }

                @Override
                public void sendMessage(@NonNull final Object message) {
                    if (message.equals(CommandBuilder.leaveGameCommand())) {
                        stop();
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
        public ObjectNode handledMessage;

        @Override
        public boolean accepts(@NonNull final ObjectNode message) {
            return true;
        }

        @Override
        public void handle(@NonNull final ObjectNode message) {
            handledMessage = message;
        }
    }

    @Test
    public void testStartSocket() {
        gameEventManager.startSocket(gameId, armyID);
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
        final String message = "{\"field\":\"value\"}";
        gameEventManager
                .addHandler(testGameEventHandler)
                .handle(message);
        Assert.assertEquals(message, testGameEventHandler.handledMessage.toString());
    }
}
