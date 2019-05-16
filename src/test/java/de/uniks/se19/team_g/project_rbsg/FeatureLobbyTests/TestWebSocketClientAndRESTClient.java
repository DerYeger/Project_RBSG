package de.uniks.se19.team_g.project_rbsg.FeatureLobbyTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.uniks.se19.team_g.project_rbsg.FeatureLobby.Logic.RESTClient;
import de.uniks.se19.team_g.project_rbsg.FeatureLobby.Logic.WebSocketClient;
import de.uniks.se19.team_g.project_rbsg.FeatureLobby.Logic.WebSocketConfigurator;
import org.junit.Test;

import java.io.IOException;

public class TestWebSocketClient
{
    @Test
    public void TestWebSocket() throws InterruptedException
    {
        RESTClient restClient = new RESTClient();
        String userKey = "nothing";
        String loginResponse = restClient.post("/user/login", null, null, "{ \"name\" : \"hello1\", \"password\" : \"hello1\" }");
        try
        {
           userKey = new ObjectMapper().readTree(loginResponse).get("data").get("userKey").asText();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        WebSocketConfigurator.userKey = userKey;
        WebSocketClient webSocketClient = new WebSocketClient("/system", this::handle);

        Thread.sleep(500);

        restClient.post("/user/login", null, null, "{ \"name\" : \"hello2\", \"password\" : \"hello2\" }");

        webSocketClient.stop();
    }

    public void handle(String message) {
        System.out.println(message);
    }
}
