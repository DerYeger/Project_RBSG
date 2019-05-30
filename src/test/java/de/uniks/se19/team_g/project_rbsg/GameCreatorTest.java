package de.uniks.se19.team_g.project_rbsg;

import de.uniks.se19.team_g.project_rbsg.server.rest.GameCreator;
import de.uniks.se19.team_g.project_rbsg.model.Game;
import de.uniks.se19.team_g.project_rbsg.model.GameBuilder;
import de.uniks.se19.team_g.project_rbsg.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;
/**
 * @author Juri Lozowoj
 */
public class GameCreatorTest {

    @Test
    public void sendGameRequestTest(){
        final User testUser = new User("Juri", "geheim");
        final GameBuilder gameBuilder = new GameBuilder();
        final Game testGame = gameBuilder.getGame("make war, not love", 4);
        testUser.setUserKey("905e064c-2ec2-49b3-930f-06fd0e49626b");

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
        AtomicReference<String> status = new AtomicReference<>();
        AtomicReference<String> message = new AtomicReference<>();
        AtomicReference<String> gameID = new AtomicReference<>();
        try {
            requestedGame.thenAccept(
                    map -> {
                        status.set((String) map.get("status"));
                        message.set((String) map.get("message"));
                        HashMap<String, Object> data = (HashMap<String, Object>) map.get("data");
                        gameID.set((String) data.get("gameId"));
                    }
            ).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertEquals("success", status.get());
        Assert.assertEquals("", message.get());
        Assert.assertEquals("5cddad15b6ae670001291945", gameID.get());
    }
}
