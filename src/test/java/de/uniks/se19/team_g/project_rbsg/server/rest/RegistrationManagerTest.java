package de.uniks.se19.team_g.project_rbsg.server.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.uniks.se19.team_g.project_rbsg.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

public class RegistrationManagerTest {

    @Test
    public void onRegistrationTest() throws InterruptedException {
        User testUser = new User("Juri", "geheim");
        RegistrationManager registrationManager = new RegistrationManager(
                new RestTemplate() {
                    @Override
                    public <T> ResponseEntity<T> exchange(String url, HttpMethod method, @Nullable HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables) throws RestClientException {
                        Assert.assertEquals(url, "https://rbsg.uniks.de/api/user");
                        final ObjectNode body = new ObjectMapper().createObjectNode().put("status", "success");
                        ResponseEntity<ObjectNode> response = new ResponseEntity<>(body, HttpStatus.OK);
                        @SuppressWarnings("unchecked")
                        ResponseEntity<T> castedResponse = (ResponseEntity<T>) response;
                        return castedResponse;
                    }
                }
        );
        CompletableFuture<ResponseEntity<ObjectNode>> registrationAnswer = registrationManager.onRegistration(testUser);
        AtomicReference<String> string = new AtomicReference<>();
        try {
            registrationAnswer.thenAccept(response -> string.set(response.getBody().get("status").asText())).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Assert.assertEquals("success", string.get());
    }

}
