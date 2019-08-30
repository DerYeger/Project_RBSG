package de.uniks.se19.team_g.project_rbsg.server.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.uniks.se19.team_g.project_rbsg.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

/**
 * @author Keanu Stückrad
 * @author Jan Müller
 */
@Component
public class LoginManager {

    private static final String URL = "https://rbsg.uniks.de/api/user/login";

    private final RestTemplate restTemplate;

    public LoginManager(@NonNull final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public CompletableFuture<ResponseEntity<ObjectNode>> callLogin(@NonNull final User user) {
        return CompletableFuture.supplyAsync(() -> doLogin(user));
    }

    @Nonnull
    public ResponseEntity<ObjectNode> doLogin(@NonNull User user) {
        return restTemplate.exchange(URL, HttpMethod.POST, new HttpEntity<>(user), ObjectNode.class);
    }

    public User login(User user) {
        ResponseEntity<ObjectNode> response = doLogin(user);

        if (response.getBody() == null) {
            throw new RuntimeException("login request returned empty body");
        }

        final ObjectNode body = response.getBody();

        if (body.get("status").asText().equals("success")) {
            final JsonNode data = body.get("data");

            if (data == null) {
                throw new RuntimeException("login response has no data object");
            }

            user.setUserKey(data.get("userKey").asText());
        }

        return user;
    }
}
