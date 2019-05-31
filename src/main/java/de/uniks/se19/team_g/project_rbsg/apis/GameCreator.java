package de.uniks.se19.team_g.project_rbsg.apis;

import de.uniks.se19.team_g.project_rbsg.model.Game;
import de.uniks.se19.team_g.project_rbsg.model.User;
import javafx.scene.control.Alert;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

/**
 * @author Juri Lozowoj
 */
@Component
public class GameCreator {

    final String uri = "https://rbsg.uniks.de/api/game";

    private RestTemplate restTemplate;

    public GameCreator(@Nullable RestTemplate restTemplate){
        this.restTemplate = (restTemplate == null) ? new RestTemplate() : restTemplate;
    }

    public CompletableFuture sendGameRequest(@Nullable User user, @Nullable Game game){
        HttpHeaders header = new HttpHeaders();

        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("neededPlayer", game.getNeededPlayer());

        if (user != null) {
            header.set("userKey", user.getUserKey());
            requestBody.put("name", game.getName());
        }

        HttpEntity<?> request = new HttpEntity<Object>(requestBody, header);
        return CompletableFuture.supplyAsync(() -> restTemplate.postForObject(uri, request, HashMap.class));
    }

    public void handleGameRequestErrors(String title, String headerText, String errorMessage){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }
}
