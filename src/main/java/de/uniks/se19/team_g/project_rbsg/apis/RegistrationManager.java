package de.uniks.se19.team_g.project_rbsg.apis;

import de.uniks.se19.team_g.project_rbsg.model.User;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class RegistrationManager {

    public boolean onRegistration(User user){
        final String uri = "https://rbsg.uniks.de/api/user";

        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> answer = restTemplate.postForObject(uri, user, Map.class);
        return true;
    }
}
