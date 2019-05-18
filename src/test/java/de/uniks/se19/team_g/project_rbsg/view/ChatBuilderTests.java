package de.uniks.se19.team_g.project_rbsg.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.RESTClient;
import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.WebSocketClient;
import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.WebSocketConfigurator;
import de.uniks.se19.team_g.project_rbsg.controller.ChatController;
import de.uniks.se19.team_g.project_rbsg.controller.ChatWebSocketCallback;
import de.uniks.se19.team_g.project_rbsg.model.User;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.lang.NonNull;
import org.testfx.framework.junit.ApplicationTest;

import java.io.IOException;

/**
 * @author Jan Müller
 */
public class ChatBuilderTests extends ApplicationTest {

    @Override
    public void start(@NonNull final Stage stage) throws IOException {
        final RESTClient restClient = new RESTClient();
        String userKey = "nothing";
        final String loginResponse = restClient.post("/user/login", null, null, "{ \"name\" : \"hello1\", \"password\" : \"hello1\" }");
        try
        {
            userKey = new ObjectMapper().readTree(loginResponse).get("data").get("userKey").asText();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        WebSocketConfigurator.userKey = userKey;
        final User user = new User("hello1", "hello1");

        final ChatWebSocketCallback webSocketCallback = new ChatWebSocketCallback();
        final WebSocketClient webSocketClient = new WebSocketClient("/chat?user=hello1", webSocketCallback);
        final ChatController chatController = new ChatController(user, webSocketClient, webSocketCallback);

        final ChatBuilder chatBuilder = new ChatBuilder(chatController);
        final Node chat = chatBuilder.getChat();
        Assert.assertNotNull(chat);

        final Scene scene = new Scene((Parent) chat);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void test() {
        Assert.assertNotNull(lookup(ChatController.GENERAL_CHANNEL_NAME).query());
        Assert.assertNotNull(lookup(".text-area").query());
        Assert.assertNotNull(lookup(".text-field").query());
    }
}
