package de.uniks.se19.team_g.project_rbsg.lobby.chat.command;

import de.uniks.se19.team_g.project_rbsg.lobby.chat.ChatChannelController;
import de.uniks.se19.team_g.project_rbsg.lobby.chat.ChatController;
import de.uniks.se19.team_g.project_rbsg.lobby.chat.ChatWebSocketCallback;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
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
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.testfx.framework.junit.ApplicationTest;

import javax.websocket.Session;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Juri Lozowoj
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        ChatController.class,
        UserProvider.class,
        ChatWebSocketCallback.class,
        ChatChannelController.class,
        ChuckNorrisCommandHandlerTest.ContextConfiguration.class
})
public class ChuckNorrisCommandHandlerTest extends ApplicationTest {


    private static String chuckJoke;

    @TestConfiguration
    static class ContextConfiguration{


        @Bean
        public WebSocketClient webSocketClient() {
            return new WebSocketClient() {
                @Override
                public void start(final @NonNull String endpoint, final @NonNull IWebSocketCallback callback) {
                }

                @Override
                public void onOpen(final Session session) throws IOException {
                }

                @Override
                public void sendMessage(final Object message) {
                }
            };
        }

    }

    @Autowired
    private ChatWebSocketCallback chatWebSocketCallback;

    @Autowired
    private UserProvider userProvider;


    @Autowired
    private WebSocketClient webSocketClient;

    @Autowired
    private ChatChannelController callback;

    @Test
    public void testChuckJoke() throws Exception {

        ChuckNorrisCommandHandler chuckNorrisCommandHandler = new ChuckNorrisCommandHandler(
                new ChatController(userProvider, webSocketClient, chatWebSocketCallback){
                    @Override
                    public void sendMessage(@NonNull final ChatChannelController callback, @NonNull final String channel, @NonNull final String content) {
                        chuckJoke = content;
                    }
                },
                new RestTemplate(){
                    @Override
                    public <T> T getForObject(String url, Class<T> responseType, Object... uriVariables) throws RestClientException {
                        String joke ="Chuck Norris does not need try-catch blocks, exceptions are too afread to raise.";
                        Assert.assertEquals(url, "https://api.chucknorris.io/jokes/random");
                        Map<String, Object> testAnswer = new HashMap<>();
                        testAnswer.put("value", joke);
                        return (T) testAnswer;
                    }
                });

        userProvider.get().setName("TestUser");

        final String chuckCommand = "/chuckMe";

        String joke ="Chuck Norris does not need try-catch blocks, exceptions are too afread to raise.";

        chuckNorrisCommandHandler.handleCommand(callback, chuckCommand);
        Thread.sleep(500);
        Assert.assertEquals(joke, chuckJoke);
    }

}