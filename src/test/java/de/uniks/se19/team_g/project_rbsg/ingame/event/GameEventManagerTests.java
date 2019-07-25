package de.uniks.se19.team_g.project_rbsg.ingame.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.uniks.se19.team_g.project_rbsg.server.websocket.IWebSocketCallback;
import de.uniks.se19.team_g.project_rbsg.server.websocket.WebSocketClient;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.lang.NonNull;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.validation.constraints.NotNull;

/**
 * @author Jan Müller
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        GameEventManager.class
})
public class GameEventManagerTests {

    @Autowired
    private GameEventManager gameEventManager;

    @MockBean
    private WebSocketClient webSocketClient;

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
    public void testStartSocket() throws Exception {
        String gameId = "12345";
        String armyID = "54321";
        gameEventManager.startSocket(gameId, armyID);
        Mockito.verify(webSocketClient).start("/game?gameId=12345&armyId=54321", gameEventManager);
    }

    @Test
    public void testTerminate() {
        gameEventManager.terminate();
        InOrder inOrder = Mockito.inOrder(webSocketClient);
        inOrder.verify(webSocketClient).setCloseHandler(gameEventManager);
        inOrder.verify(webSocketClient).isClosed();
        inOrder.verify(webSocketClient).sendMessage(ArgumentMatchers.eq(CommandBuilder.leaveGameCommand()));
        inOrder.verify(webSocketClient).stop();
        inOrder.verifyNoMoreInteractions();
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


    @Test
    public void testSendEndPhaseCommand(){

        GameEventManager gameEventManager = new GameEventManager(new WebSocketClient() {
            ObjectNode objectNode = new ObjectMapper()
                    .createObjectNode()
                    .put("messageType", "command")
                    .put("action", "nextPhase");

            @Override
            public void sendMessage(final Object message) {
                Assert.assertEquals(objectNode, message);
            }

            @Override
            public void start(final @NotNull String endpoint, final @NotNull IWebSocketCallback wsCallback) throws Exception{

            }
        });


        gameEventManager.sendEndPhaseCommand();

    }

    @Test
    public void defaultConstructor() {
        GameEventManager gameEventManager = new GameEventManager(new WebSocketClient());
        Assert.assertSame(gameEventManager.api().getGameEventManager(), gameEventManager);
    }
}
