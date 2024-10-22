package de.uniks.se19.team_g.project_rbsg.server.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.uniks.se19.team_g.project_rbsg.model.Game;
import de.uniks.se19.team_g.project_rbsg.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;


public class JoinGameManagerTest {

    @Test
    public void joinGameTest() {
        JoinGameManager joinGameManager = new JoinGameManager(
                new RestTemplate() {
                    @Override
                    public <T> ResponseEntity<T> getForEntity(@Nonnull String url, @Nonnull Class<T> responseType, @Nonnull Object... uriVariables) throws RestClientException {
                        Assert.assertEquals("https://rbsg.uniks.de/api/game/5ce6e24550487200013b9d19", url);
                        HttpHeaders responseHeaders = new HttpHeaders();
                        String testAnswer = "{\"status\": \"success\"}";
                        return new ResponseEntity<T> ((T) testAnswer, responseHeaders, HttpStatus.OK);
                    }
                }
        );
        final User testUser = new User("Juri", "geheim");
        testUser.setUserKey("a1292282-0418-4b00-bd4a-97982bee7faf");
        final Game testGame = new Game("5ce6e24550487200013b9d19", "SuperGame", 4, 1);
        CompletableFuture<ResponseEntity<String>> joinedGame = joinGameManager.joinGame(testUser, testGame);
        AtomicReference<String> status = new AtomicReference<>();
        try {
            joinedGame.thenAccept(
                    responseEntity -> {
                        String answer = responseEntity.getBody();
                        try {
                            status.set( new ObjectMapper().readTree(answer).get("status").asText());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
            ).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        Assert.assertEquals("success", status.get());
    }

    @Test
    public void joinGameAsSpectatorTest() {
        JoinGameManager joinGameManager = new JoinGameManager(
                new RestTemplate() {
                    @Override
                    public <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, Object... uriVariables) throws RestClientException {
                        Assert.assertEquals("https://rbsg.uniks.de/api/game/5ce6e24550487200013b9d19?spectator=true", url);
                        HttpHeaders responseHeaders = new HttpHeaders();
                        String testAnswer = "{\"status\": \"success\"}";
                        return new ResponseEntity<T> ((T) testAnswer, responseHeaders, HttpStatus.OK);
                    }
                }
        );
        final User testUser = new User("Juri", "geheim");
        testUser.setUserKey("a1292282-0418-4b00-bd4a-97982bee7faf");
        final Game testGame = new Game("5ce6e24550487200013b9d19", "SuperGame", 4, 1);
        testGame.setSpectatorModus(true);
        CompletableFuture<ResponseEntity<String>> joinedGame = joinGameManager.joinGame(testUser, testGame);
        AtomicReference<String> status = new AtomicReference<>();
        try {
            joinedGame.thenAccept(
                    responseEntity -> {
                        String answer = responseEntity.getBody();
                        try {
                            status.set( new ObjectMapper().readTree(answer).get("status").asText());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
            ).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        Assert.assertEquals("success", status.get());
    }
}
