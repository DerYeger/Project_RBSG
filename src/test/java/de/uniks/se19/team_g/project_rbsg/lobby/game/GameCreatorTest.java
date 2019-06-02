package de.uniks.se19.team_g.project_rbsg.lobby.game;

import de.uniks.se19.team_g.project_rbsg.model.Game;
import de.uniks.se19.team_g.project_rbsg.model.User;
import de.uniks.se19.team_g.project_rbsg.server.rest.GameCreator;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
/**
 * @author Juri Lozowoj
 */
public class GameCreatorTest {

    @Test
    public void sendGameRequestTest(){
        final User testUser = new User("Juri", "geheim");
        final Game testGame = new Game("make war, not love", 4);
        testUser.setUserKey("acc92f67-c6ee-45c5-a77d-4616db030a04");

        final GameCreator gameCreator = new GameCreator(
                new RestTemplate(){

                    @Override
                    public <T> T postForObject(String url, @Nullable Object request, Class<T> responseType, Object... uriVariables) throws RestClientException {
                        Assert.assertEquals(url, "https://rbsg.uniks.de/api/game");
                        Map<String, Object> testAnswer = new HashMap<>();
                        testAnswer.put("status", "success");
                        testAnswer.put("message", "");
                        HashMap<String, Object> data = new HashMap<>();
                        data.put("gameId", "5cddad15b6ae670001291945");
                        testAnswer.put("data", data);
                        return (T) testAnswer;
                    }

                }

        );

        CompletableFuture<HashMap<String, Object>> requestedGame = gameCreator.sendGameRequest(testUser, testGame);

        HashMap<String, Object> map = null;
        try {
            map = requestedGame.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        Assert.assertEquals("success", map.get("status"));
        Assert.assertEquals("",  map.get("message"));
        Assert.assertEquals("5cddad15b6ae670001291945", ((HashMap<String, Object>)map.get("data")).get("gameId"));
    }
}
