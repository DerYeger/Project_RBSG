package de.uniks.se19.team_g.project_rbsg.server.rest;

import de.uniks.se19.team_g.project_rbsg.model.User;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

/**
 * @author Juri Lozowoj
 */
@Component
public class RegistrationManager {

    final private String uri = "https://rbsg.uniks.de/api/user";

    final private RestTemplate restTemplate;

    public RegistrationManager(@NonNull final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public CompletableFuture onRegistration(@NonNull User user) {
        return CompletableFuture.supplyAsync(() -> restTemplate.postForObject(uri, user, HashMap.class));
    }

}