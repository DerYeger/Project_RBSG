package de.uniks.se19.team_g.project_rbsg.server.rest;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.uniks.se19.team_g.project_rbsg.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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

    public CompletableFuture<ResponseEntity<ObjectNode>> onLogin(@NonNull final User user) {
        return CompletableFuture.supplyAsync(() -> restTemplate.exchange(URL, HttpMethod.POST, new HttpEntity<>(user), ObjectNode.class));
    }
}
