package de.uniks.se19.team_g.project_rbsg.server.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.uniks.se19.team_g.project_rbsg.model.Game;
import de.uniks.se19.team_g.project_rbsg.model.User;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

@Category(OnlineTests.class)
@Ignore
public class OnlineRESTManagerTest {

    @Test
    public void loginOnlineTest() throws InterruptedException, ExecutionException {
        LoginManager loginManager = new LoginManager(new RestTemplate());
        User testUser = new User("TestUser123", "geheim");
        CompletableFuture<ResponseEntity<ObjectNode>> loginAnswer = loginManager.callLogin(testUser);
        AtomicReference<String> string = new AtomicReference<>();
        loginAnswer.thenAccept(response -> string.set(response.getBody().get("status").asText())).get();
        Assert.assertEquals("success", string.get());
    }

    @Test
    public void createGameOnlineTest() throws InterruptedException, ExecutionException {
        final RestTemplate restTemplate = new RestTemplate();
        final LoginManager loginManager = new LoginManager(restTemplate);
        final GameCreator gameCreator = new GameCreator(restTemplate);
        final User testUser = new User("TestUser123", "geheim");
        final Game testGame = new Game("make war, not love", 4);

        CompletableFuture<ResponseEntity<ObjectNode>> loginAnswer = loginManager.callLogin(testUser);
        AtomicReference<String> loginStatus = new AtomicReference<>();
        loginAnswer.thenAccept(response -> loginStatus.set(response.getBody().get("status").asText())).get();
        Assert.assertEquals("success", loginStatus.get());

        final ResponseEntity<ObjectNode> loginAnswerMap = loginAnswer.get();
        final JsonNode data = loginAnswerMap.getBody().get("data");
        final String userKey = data.get("userKey").asText();
        testUser.setUserKey(userKey);

        CompletableFuture<HashMap<String, Object>> requestedGame = gameCreator.sendGameRequest(testUser, testGame);
        AtomicReference<String> gameRequestStatus = new AtomicReference<>();
        requestedGame.thenAccept(map -> gameRequestStatus.set((String) map.get("status"))).get();
        Assert.assertEquals("success", gameRequestStatus.get());
    }

    @Test
    public void joinGameOnlineTest() throws InterruptedException, ExecutionException {
        final RestTemplate restTemplate = new RestTemplate();
        final LoginManager loginManager = new LoginManager(restTemplate);
        final GameCreator gameCreator = new GameCreator(restTemplate);
        final JoinGameManager joinGameManager = new JoinGameManager(restTemplate);

        final User testUser = new User("TestUser123", "geheim");
        final Game testGame = new Game("make war, not love", 4);
        CompletableFuture<ResponseEntity<ObjectNode>> loginRequest = loginManager.callLogin(testUser);
        AtomicReference<String> loginStatus = new AtomicReference<>();
        loginRequest.thenAccept(response -> loginStatus.set(response.getBody().get("status").asText())).get();
        Assert.assertEquals("success", loginStatus.get());

        final ResponseEntity<ObjectNode> loginAnswer = loginRequest.get();
        final JsonNode loginData = loginAnswer.getBody().get("data");
        final String userKey = loginData.get("userKey").asText();
        testUser.setUserKey(userKey);

        CompletableFuture<HashMap<String, Object>> createGameRequest = gameCreator.sendGameRequest(testUser, testGame);
        AtomicReference<String> gameRequestStatus = new AtomicReference<>();
        createGameRequest.thenAccept(map -> gameRequestStatus.set((String) map.get("status"))).get();
        Assert.assertEquals("success", gameRequestStatus.get());

        final HashMap<String, Object> createGameAnswer = createGameRequest.get();
        final HashMap<String, Object> createGameData = (HashMap<String, Object>) createGameAnswer.get("data");
        final String gameId = ((String) createGameData.get("gameId"));
        testGame.setId(gameId);

        CompletableFuture<ResponseEntity<String>> joinGameRequest = joinGameManager.joinGame(testUser, testGame);
        AtomicReference<String> joinRequestStatus = new AtomicReference<>();
        joinGameRequest.thenAccept(
                responseEntity -> {
                    String answer = responseEntity.getBody();
                    try {
                        joinRequestStatus.set(new ObjectMapper().readTree(answer).get("status").asText());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).get();
        Assert.assertEquals("success", gameRequestStatus.get());
    }
}
