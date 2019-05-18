package de.uniks.se19.team_g.project_rbsg.apis;

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

    final String BASE_REST_URL = "https://rbsg.uniks.de/api/user";

    private RestTemplate restTemplate;

    public RegistrationManager(@NonNull final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public CompletableFuture onRegistration(@NonNull final User user){
        return CompletableFuture.supplyAsync(() -> restTemplate.postForObject(BASE_REST_URL + "/user", user, HashMap.class));
    }
}
