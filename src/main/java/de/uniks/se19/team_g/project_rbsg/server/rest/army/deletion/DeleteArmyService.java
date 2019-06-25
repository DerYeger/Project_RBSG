package de.uniks.se19.team_g.project_rbsg.server.rest.army.deletion;

import de.uniks.se19.team_g.project_rbsg.model.Army;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.deletion.requests.DeleteArmyRequest;
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
    private final String URL = "/army";
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public DeleteArmyService(@NonNull RestTemplate restTemplate){

        this.rbsgTemplate = restTemplate;
    }

    public void deleteArmy(Army army){

        String deleteArmyUrl = "/army/" + army.id.get();
        DeleteArmyRequest deleteArmyRequest = new DeleteArmyRequest();

        rbsgTemplate.delete(deleteArmyUrl);

        /**return CompletableFuture.supplyAsync(() -> rbsgTemplate.exchange(
                deleteArmyUrl,
                HttpMethod.DELETE,
                new HttpEntity<>(deleteArmyRequest),
                DeleteArmyResponse.class))
                .thenApply(deleteArmyResponse -> onDeletionResponseReturned(deleteArmyResponse));**/
    }

    private DeleteArmyResponse onDeletionResponseReturned(ResponseEntity<DeleteArmyResponse> responseEntity) {
        DeleteArmyResponse deleteArmyResponse = new DeleteArmyResponse();

        if(responseEntity.getBody().message.isEmpty()){
            logger.debug("Delete-Request: Server responded with empty message.");
        }
        else{
            logger.debug("Delete-Request: Server responded with message: " + responseEntity.getBody().message);
        }
        if(responseEntity.getBody().status.isEmpty()){
            logger.debug("Server responded with empty status.");
        }
        else{
            logger.debug("Delete-Request: Server responded with status: " + responseEntity.getBody().status);
        }
        if(responseEntity.getBody().data!=null){
            logger.debug("Server responded with data: " + responseEntity.getBody().data.toString());
        }
        else{
            logger.debug("Delete-Request: Server responded with data: " + responseEntity.getBody().data.toString());
        }

        if(!responseEntity.getBody().status.equals("success")){
            logger.debug("An error occurred.");
            return null;
        }
        else{
            deleteArmyResponse.status=responseEntity.getBody().status;
            deleteArmyResponse.message=responseEntity.getBody().message;
            deleteArmyResponse.data=responseEntity.getBody().data;
        }
        return null;
    }
}
