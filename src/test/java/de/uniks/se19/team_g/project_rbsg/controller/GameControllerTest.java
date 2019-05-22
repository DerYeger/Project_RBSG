package de.uniks.se19.team_g.project_rbsg.controller;

import de.uniks.se19.team_g.project_rbsg.model.Game;
import de.uniks.se19.team_g.project_rbsg.model.GameBuilder;
import de.uniks.se19.team_g.project_rbsg.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public class GameControllerTest {

    @Test
    public void joinGameTest(){
        GameController gameController = new GameController(new RestTemplate());
        final User testUser = new User("Juri", "geheim");
        testUser.setUserKey("ad904304-205f-4eb3-8020-4a0bf1244104");
        final Game testGame = new GameBuilder().getGame("SuperGame", 4);
        testGame.setGameId("5ce5839750487200013b9b16");

        CompletableFuture<HashMap<String, Object>> joinedGame = gameController.joinGame(testUser, testGame);
        AtomicReference<String> status = new AtomicReference<>();
        AtomicReference<String> message = new AtomicReference<>();
        joinedGame.thenAccept(
                map -> {
                    status.set((String) map.get("status"));
                    message.set((String) map.get("message"));
                }
        ).join();

        Assert.assertEquals("success", status.get());
    }
}
