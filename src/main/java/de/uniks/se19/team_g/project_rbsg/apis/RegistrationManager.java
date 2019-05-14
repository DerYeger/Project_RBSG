package de.uniks.se19.team_g.project_rbsg.apis;

import de.uniks.se19.team_g.project_rbsg.model.User;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class RegistrationManager {

    private RestTemplate restTemplate;

    public RegistrationManager(@Nullable RestTemplate restTemplate) {
        this.restTemplate = restTemplate == null ? new RestTemplate() : restTemplate;
    }

    public boolean onRegistration(@NonNull User user){
        final String uri = "https://rbsg.uniks.de/api/user";

        Map<String, Object> answer = restTemplate.postForObject(uri, user, Map.class);
        if(answer.get("status").equals("success")){
            return true;
        } else {
            return false;
        }
    }
}
