package de.uniks.se19.team_g.project_rbsg.apis;

import de.uniks.se19.team_g.project_rbsg.model.User;
import javafx.scene.control.Alert;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

/**
 * @author Juri Lozowoj
 */
@Component
public class GameManager {

    final String uri = "https://rbsg.uniks.de/api/game";

    private RestTemplate restTemplate;

    public GameManager(@Nullable RestTemplate restTemplate){
        this.restTemplate = (restTemplate == null) ? new RestTemplate() : restTemplate;
    }

    public CompletableFuture sendGameRequest(@Nullable User user, @Nullable int numberOfPlayers){
        HttpHeaders header = new HttpHeaders();

        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("neededPlayer", numberOfPlayers);

        if (user != null) {
            header.set("userKey", user.getUserKey());
            requestBody.put("name", user.getName());
        }

        HttpEntity<?> request = new HttpEntity<Object>(requestBody, header);
        try {
            return CompletableFuture.supplyAsync(() -> restTemplate.postForObject(uri, request, HashMap.class));
        }catch (RestClientResponseException e){
            handleGameRequestErrors("Fehler", "Fehler bei der Spielerstellung", "Keine Antwort vom Server");
        }
        return null;
    }

    public void handleGameRequestErrors(String title, String headerText, String errorMessage){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }
}
