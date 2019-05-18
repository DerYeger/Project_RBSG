package de.uniks.se19.team_g.project_rbsg.apis;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.BaseRequest;
import de.uniks.se19.team_g.project_rbsg.model.User;
import javafx.scene.control.Alert;
import org.json.JSONObject;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.json.Json;
import javax.json.JsonObject;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author Keanu Stückrad
 */
@Component
public class LoginManager {

    public static final String BASE_REST_URL = "https://rbsg.uniks.de/api";

    public BaseRequest baseRequest;

    public LoginManager(BaseRequest baseRequest){
        this.baseRequest = baseRequest;
    }

    public User onLogin(@NonNull final User user) throws ExecutionException, InterruptedException, UnirestException {
        // get a JsonNode as response from server
        JsonObject body = Json.createObjectBuilder()
                .add("name", user.getName())
                .add("password", user.getPassword())
                .build();
        baseRequest = Unirest.post(BASE_REST_URL + "/user/login").body(body.toString());
        Future<HttpResponse<JsonNode>> future = baseRequest.asJsonAsync();
        HttpResponse<JsonNode> response = future.get();

        JsonNode jsonNode = response.getBody();
        String status = jsonNode.getObject().getString("status");
        if (!status.equals("failure")) {
            // save userKey and return User Clone
            JSONObject data = jsonNode.getObject().getJSONObject("data");
            String userKey = data.getString("userKey");
            return new User(user, userKey);
        } else {
            invalidCredentialsAlert(status, jsonNode.getObject().getString("message"));
        }
        return null;
    }

    public static void invalidCredentialsAlert(@NonNull final String status, @NonNull final String message) {
        // alert failure when typing invalid credentials
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(status);
        alert.setHeaderText("Login failed");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void noConnectionAlert() {
        // alert failure when there is no server connection
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("failure");
        alert.setHeaderText("Login failed");
        alert.setContentText("No server connection");
        alert.showAndWait();
    }

}
