package de.uniks.se19.team_g.project_rbsg.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.RESTClient;
import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.WebSocketClient;
import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.WebSocketConfigurator;
import de.uniks.se19.team_g.project_rbsg.controller.ChatController;
import de.uniks.se19.team_g.project_rbsg.controller.ChatWebSocketCallback;
import de.uniks.se19.team_g.project_rbsg.model.User;
import de.uniks.se19.team_g.project_rbsg.view.ChatBuilder;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.validation.constraints.NotNull;
import java.io.IOException;

/**
 * @author Jan Müller
 */
public class ChatTestFXApplication extends Application {

    private static String userName;

    public static void main(String[] args) {
        userName = args[0];
        launch(args);
    }

    @Override
    public void start(@NotNull final Stage primaryStage) throws IOException {
        final RESTClient restClient = new RESTClient();
        String userKey = "nothing";
        final String loginResponse = restClient.post("/user/login", null, null, "{ \"name\" : \"" + userName + "\", \"password\" : \"" + userName + "\" }");
        System.out.println(userName);
        try
        {
            userKey = new ObjectMapper().readTree(loginResponse).get("data").get("userKey").asText();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        WebSocketConfigurator.userKey = userKey;
        final User user = new User(userName, userName);

        final ChatWebSocketCallback webSocketCallback = new ChatWebSocketCallback();
        final WebSocketClient webSocketClient = new WebSocketClient("/chat?user=" + userName, webSocketCallback);
        final ChatController chatController = new ChatController(user, webSocketClient, webSocketCallback);

        final ChatBuilder chatBuilder = new ChatBuilder(chatController);
        final Node chat = chatBuilder.getChat();

        final Scene scene = new Scene((Parent) chat);

        primaryStage.setWidth(400);
        primaryStage.setHeight(200);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);

        primaryStage.show();
    }
}
