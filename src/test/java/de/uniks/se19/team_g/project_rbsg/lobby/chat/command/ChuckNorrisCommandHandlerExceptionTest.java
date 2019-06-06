package de.uniks.se19.team_g.project_rbsg.lobby.chat.command;

import de.uniks.se19.team_g.project_rbsg.configuration.JavaConfig;
import de.uniks.se19.team_g.project_rbsg.lobby.chat.ChatChannelController;
import de.uniks.se19.team_g.project_rbsg.lobby.chat.ChatController;
import de.uniks.se19.team_g.project_rbsg.lobby.chat.ChatWebSocketCallback;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.server.websocket.IWebSocketCallback;
import de.uniks.se19.team_g.project_rbsg.server.websocket.WebSocketClient;
import de.uniks.se19.team_g.project_rbsg.termination.Terminator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.testfx.framework.junit.ApplicationTest;

import javax.websocket.Session;
import java.io.IOException;

/**
 * @author Juri Lozowoj
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        JavaConfig.class,
        ChatController.class,
        UserProvider.class,
        ChatWebSocketCallback.class,
        ChatChannelController.class,
        ChatController.class,
        Terminator.class,
        ChuckNorrisCommandHandlerTest.ContextConfiguration.class
})
public class ChuckNorrisCommandHandlerExceptionTest extends ApplicationTest {

    private static String errorMessageJoke;

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
    private UserProvider userProvider;

    @Autowired
    private ChatController chatController;

    @Test
    public void testException() throws Exception {

        ChuckNorrisCommandHandler chuckNorrisCommandHandler = new ChuckNorrisCommandHandler(chatController,
                new RestTemplate(){
                    @Override
                    public <T> T getForObject(String url, Class<T> responseType, Object... uriVariables) throws RestClientException {
                        throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                });

        userProvider.get().setName("TestUser");

        final String chuckCommand = "/chuckMe";

        String errorJoke = "Error - Chuck Norris is to funny for you";

        chuckNorrisCommandHandler.handleCommand(new ChatChannelController(){
            @Override
            public void displayMessage(@NonNull final String from, @NonNull final String content) {
                errorMessageJoke = content;
            }

        }, chuckCommand);
        Thread.sleep(500);
        Assert.assertEquals(errorJoke, errorMessageJoke);
    }
}

