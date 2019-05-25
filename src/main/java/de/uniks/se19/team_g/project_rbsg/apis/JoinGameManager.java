package de.uniks.se19.team_g.project_rbsg.apis;

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

import java.util.concurrent.CompletableFuture;

@Component
public class JoinGameManager {

    final String uri = "https://rbsg.uniks.de/api/game/";

    private RestTemplate restTemplate;

    public JoinGameManager(@Nullable RestTemplate restTemplate){
        this.restTemplate = ((restTemplate == null) ? new RestTemplate() : restTemplate);
    }

    public CompletableFuture joinGame(@NonNull User user, @NonNull Game game){
        HttpHeaders header = new HttpHeaders();
        header.set("userKey", user.getUserKey());

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(uri)
                .queryParam(game.getGameId());

        HttpEntity<?> request = new HttpEntity<Object>("", header);

        return CompletableFuture.supplyAsync(() -> this.restTemplate.exchange(
                uriBuilder.toUriString().replace("?", ""),
                HttpMethod.GET,
                request,
                String.class));
    }
}
