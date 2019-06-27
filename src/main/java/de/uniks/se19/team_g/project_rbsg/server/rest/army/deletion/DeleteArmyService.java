package de.uniks.se19.team_g.project_rbsg.server.rest.army.deletion;

import de.uniks.se19.team_g.project_rbsg.model.Army;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.deletion.serverResponses.DeleteArmyResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Component
public class DeleteArmyService {

    private final RestTemplate rbsgTemplate;
    private final String URL = "/army/";
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public DeleteArmyService(@NonNull RestTemplate restTemplate) {

        this.rbsgTemplate = restTemplate;
    }

    public CompletableFuture<DeleteArmyResponse> deleteArmy(Army army) {

        String deleteArmyUrl = URL + army.id.get();

        //rbsgTemplate.delete(deleteArmyUrl);
        return CompletableFuture.supplyAsync(() -> rbsgTemplate.exchange(
                deleteArmyUrl,
                HttpMethod.DELETE,
                new HttpEntity<>(null),
                DeleteArmyResponse.class))
                .thenApply(response -> onDeletionResponseReturned(response));

        //ToDO: Auto-Save after deletion. Otherwise local and remote state are diverged.
    }

    private DeleteArmyResponse onDeletionResponseReturned(ResponseEntity<DeleteArmyResponse> response) {
        DeleteArmyResponse deleteArmyResponse = new DeleteArmyResponse();

        deleteArmyResponse.status = response.getBody().status;
        deleteArmyResponse.message = response.getBody().message;
        deleteArmyResponse.data = response.getBody().data;

        return deleteArmyResponse;
    }
}
