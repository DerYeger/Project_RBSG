package de.uniks.se19.team_g.project_rbsg.apis;

import de.uniks.se19.team_g.project_rbsg.model.User;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Keanu Stückrad
 */
@Component
public class LoginMangerTest {

    @Test
    public void onLoginTest() throws InterruptedException {
        User testUser = new User("MasterChief", "john-117");
        LoginManager loginManager = new LoginManager(
                new RestTemplate() {
                    @Override
                    public <T> T postForObject(String url, @Nullable Object request, Class<T> responseType, Object... uriVariables) throws RestClientException {
                        Assert.assertTrue(request instanceof User);
                        Assert.assertEquals(url, "https://rbsg.uniks.de/api/user/login");
                        Map<String, Object> testAnswer = new HashMap<>();
                        testAnswer.put("status", "success");
                        return (T) testAnswer;
                    }
                }
        );
        CompletableFuture<HashMap<String, Object>> registrationAnswer = loginManager.onLogin(testUser);
        AtomicReference<String> string = new AtomicReference<>();
        try {
            registrationAnswer.thenAccept(
                    map -> {
                        string.set((String) map.get("status"));
                    }
            ).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Assert.assertEquals("success", string.get());
    }

}
