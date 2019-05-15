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

    private RestTemplate restTemplate;

    public RegistrationManager(){
        this.restTemplate = new RestTemplate();
    }

    public RegistrationManager(@Nullable RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public HashMap<String, Object> onRegistration(@NonNull User user){

        try {
            return CompletableFuture.supplyAsync(() -> {
                HashMap<String, Object> serverAnswer = restTemplate.postForObject(uri, user, HashMap.class);
                return serverAnswer;
            }).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
