package de.uniks.se19.team_g.project_rbsg.apis;

import de.uniks.se19.team_g.project_rbsg.model.User;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author Juri Lozowoj
 */
@Component
public class RegistrationManager {

    final String uri = "https://rbsg.uniks.de/api/user";

    final private RestTemplate restTemplate;

    public RegistrationManager(){
        this.restTemplate = new RestTemplate();
    }

    public RegistrationManager(@Nullable RestTemplate restTemplate) {
        this.restTemplate = (restTemplate == null) ? new RestTemplate() : restTemplate;
    }

    public CompletableFuture onRegistration(@NonNull User user){
        return CompletableFuture.supplyAsync(() -> restTemplate.postForObject(uri, user, HashMap.class));
    }
}
