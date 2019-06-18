package de.uniks.se19.team_g.project_rbsg.chat.command;

import de.uniks.se19.team_g.project_rbsg.chat.ui.ChatTabManager;
import de.uniks.se19.team_g.project_rbsg.configuration.JavaConfig;
import de.uniks.se19.team_g.project_rbsg.chat.ChatChannelController;
import de.uniks.se19.team_g.project_rbsg.chat.ChatController;
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
import org.testfx.util.WaitForAsyncUtils;

import javax.websocket.Session;

/**
 * @author Juri Lozowoj
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        JavaConfig.class,
        ChatController.class,
        ChatCommandManager.class,
        ChatTabManager.class,
        UserProvider.class,
        ChatChannelController.class,
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
                public void onOpen(final Session session) {
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
                    public <T> T getForObject(@NonNull final String url, Class<T> responseType, @NonNull final Object... uriVariables) throws RestClientException {
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
        WaitForAsyncUtils.waitForFxEvents();
        Assert.assertEquals(errorJoke, errorMessageJoke);
    }
}

