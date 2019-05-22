package de.uniks.se19.team_g.project_rbsg.controller;

import de.uniks.se19.team_g.project_rbsg.model.Game;
import de.uniks.se19.team_g.project_rbsg.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

@Component
public class GameController {

    final String uri = "https://rbsg.uniks.de/api/game/:id";

    private RestTemplate restTemplate;

    public GameController(@Nullable RestTemplate restTemplate){
        this.restTemplate = ((restTemplate == null) ? new RestTemplate() : restTemplate);
    }

    public CompletableFuture joinGame(@NonNull User user, @NonNull Game game){
        HttpHeaders header = new HttpHeaders();
        HashMap<String, Object> requestBody = new HashMap<>();
        header.set("userKey", user.getUserKey());

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(uri)
                .queryParam("id", game.getGameId());

        HttpEntity<?> request = new HttpEntity<Object>(requestBody, header);

        return CompletableFuture.supplyAsync(() -> this.restTemplate.exchange(
                uriBuilder.toUriString(),
                HttpMethod.GET,
                request,
                HashMap.class));
    }
}
