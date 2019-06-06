package de.uniks.se19.team_g.project_rbsg.server.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.uniks.se19.team_g.project_rbsg.model.Game;
import de.uniks.se19.team_g.project_rbsg.model.User;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

@Ignore
public class OnlineRESTManagerTest {

    @Test
    public void loginOnlineTest() throws InterruptedException, ExecutionException {
        LoginManager loginManager = new LoginManager(new RestTemplate());
        User testUser = new User("TestUser123", "geheim");
        CompletableFuture<HashMap<String, Object>> loginAnswer = loginManager.onLogin(testUser);
        AtomicReference<String> string = new AtomicReference<>();
        loginAnswer.thenAccept(map -> string.set((String) map.get("status"))).get();
        Assert.assertEquals("success", string.get());
    }

    @Test
    public void createGameOnlineTest() throws InterruptedException, ExecutionException {
        final RestTemplate restTemplate = new RestTemplate();
        final LoginManager loginManager = new LoginManager(restTemplate);
        final GameCreator gameCreator = new GameCreator(restTemplate);
        final User testUser = new User("TestUser123", "geheim");
        final Game testGame = new Game("make war, not love", 4);

        CompletableFuture<HashMap<String, Object>> loginAnswer = loginManager.onLogin(testUser);
        AtomicReference<String> loginStatus = new AtomicReference<>();
        loginAnswer.thenAccept(map -> loginStatus.set((String) map.get("status"))).get();
        Assert.assertEquals("success", loginStatus.get());

        final HashMap<String, Object> loginAnswerMap = loginAnswer.get();
        final HashMap<String, Object> data = (HashMap<String, Object>) loginAnswerMap.get("data");
        final String userKey = ((String) data.get("userKey"));
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
        CompletableFuture<HashMap<String, Object>> loginRequest = loginManager.onLogin(testUser);
        AtomicReference<String> loginStatus = new AtomicReference<>();
        loginRequest.thenAccept(map -> loginStatus.set((String) map.get("status"))).get();
        Assert.assertEquals("success", loginStatus.get());

        final HashMap<String, Object> loginAnswer = loginRequest.get();
        final HashMap<String, Object> loginData = (HashMap<String, Object>) loginAnswer.get("data");
        final String userKey = ((String) loginData.get("userKey"));
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
