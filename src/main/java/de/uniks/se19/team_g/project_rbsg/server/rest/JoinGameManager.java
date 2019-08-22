package de.uniks.se19.team_g.project_rbsg.server.rest;

import de.uniks.se19.team_g.project_rbsg.model.Game;
import de.uniks.se19.team_g.project_rbsg.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.concurrent.CompletableFuture;

@Component
public class JoinGameManager {

    final String uri = "https://rbsg.uniks.de/api/game/";

    final String spectator = "?spectator=true";

    private RestTemplate restTemplate;

    public JoinGameManager(@Nullable RestTemplate restTemplate){
        this.restTemplate = ((restTemplate == null) ? new RestTemplate() : restTemplate);
    }

    public CompletableFuture<ResponseEntity<String>> joinGame(@NonNull User user, @NonNull Game game){
        return CompletableFuture.supplyAsync(() -> doJoinGame(user, game));
    }

    public ResponseEntity<String> doJoinGame(@NonNull User user, @NonNull Game game) {

        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromHttpUrl(uri)
                .path(game.getId());

        if (game.isSpectatorModus()){
            uriBuilder.queryParam("spectator", true);
        }

        return restTemplate.getForEntity(uriBuilder.toUriString(), String.class);
    }
}
