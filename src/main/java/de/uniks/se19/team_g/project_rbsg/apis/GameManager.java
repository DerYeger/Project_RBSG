package de.uniks.se19.team_g.project_rbsg.apis;

import de.uniks.se19.team_g.project_rbsg.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class GameManager {

    final String uri = "https://rbsg.uniks.de/api/game";

    private RestTemplate restTemplate;

    public GameManager(@Nullable RestTemplate restTemplate){
        this.restTemplate = (restTemplate == null) ? new RestTemplate() : restTemplate;
    }

    public CompletableFuture sendGameRequest(@Nullable User user, @Nullable int numberOfPlayers){
        HttpHeaders header = new HttpHeaders();
        header.set("userKey", user.getUserKey());

        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", user.getName());
        requestBody.put("neededPlayer", numberOfPlayers);

        HttpEntity<?> request = new HttpEntity<Object>(requestBody, header);

        return CompletableFuture.supplyAsync(() -> restTemplate.postForObject(uri, request, HashMap.class));

    }


}
