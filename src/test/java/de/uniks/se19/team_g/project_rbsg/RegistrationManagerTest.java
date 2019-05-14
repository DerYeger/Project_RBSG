package de.uniks.se19.team_g.project_rbsg;

import de.uniks.se19.team_g.project_rbsg.apis.RegistrationManager;
import de.uniks.se19.team_g.project_rbsg.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class RegistrationManagerTest {

    @Test
    public void onRegistrationTest(){
        User testUser = new User("Juri", "geheim");
        RegistrationManager registrationManager = new RegistrationManager(
                new RestTemplate() {
                    @Override
                    public <T> T postForObject(String url, @Nullable Object request, Class<T> responseType, Object... uriVariables) throws RestClientException {
                        Assert.assertTrue(request instanceof User);
                        Assert.assertEquals(url, "https://rbsg.uniks.de/api/user");

                        Map<String, Object> testAnswer = new HashMap<>();
                        testAnswer.put("status", "success");
                        return (T) testAnswer;
                    }
                }
        );
        HashMap<String, Object> registrationAnswer = registrationManager.onRegistration(testUser);
        Assert.assertNotNull(registrationAnswer);

    }
}
