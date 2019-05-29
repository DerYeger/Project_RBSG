package de.uniks.se19.team_g.project_rbsg.LobbyTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.RESTClient;
import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.WebSocketClient;
import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.WebSocketConfigurator;
import org.junit.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Timer;

import static org.junit.Assert.*;

/**
 * @author Georg Siebert
 */

public class WebSocketClientAndRESTClientTest
{
    private final String bodyLogin = "{ \"name\" : \"hello1\", \"password\" : \"hello1\" }";
    private final String bodyCreateGame = "{ \"name\" : \"gameofHello\", \"neededPlayer\" : 4 }";

    @Test
    public void TestRESTClient() {
        RESTClient restClient = new RESTClient(new RestTemplate());
        String userKey = "nothing";
        String loginResponse = restClient.post("/user/login", null, null, bodyLogin);
        System.out.println(loginResponse);
        try
        {
            userKey = new ObjectMapper().readTree(loginResponse).get("data").get("userKey").asText();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        LinkedMultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        header.add("userKey", userKey);
        String gameId = "nothing";
        String createGameResponse = restClient.post("/game", header, null, bodyCreateGame);
        System.out.println(createGameResponse);
        try
        {
            gameId = new ObjectMapper().readTree(createGameResponse).get("data").get("gameId").asText();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        System.out.println(gameId);

        System.out.println(restClient.delete("/game/"+gameId, header, null));
    }

    @Test
    public void TestWebSocketClient() throws InterruptedException
    {
        RESTClient restClient = new RESTClient(new RestTemplate());
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
        WebSocketClient webSocketClient = new WebSocketClient();
        webSocketClient.start("/system", this::handle);

        Thread.sleep(500);

        String post = restClient.post("/user/login", null, null, "{ \"name\" : \"hello2\", \"password\" : \"hello2\" }");
        String userKey2 = "";

        try
        {
            userKey2 = new ObjectMapper().readTree(post).get("data").get("userKey").asText();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        LinkedMultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        header.add("userKey", userKey2);
        restClient.get("/user/logout", header);

        webSocketClient.stop();
    }

    private void handle(String message) {
        System.out.println(message);
        assertEquals("{\"action\":\"userJoined\",\"data\":{\"name\":\"hello2\"}}", message);
    }
}
