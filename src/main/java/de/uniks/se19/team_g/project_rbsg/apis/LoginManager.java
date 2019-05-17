package de.uniks.se19.team_g.project_rbsg.apis;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.BaseRequest;
import de.uniks.se19.team_g.project_rbsg.model.User;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.lang.NonNull;

import javax.json.Json;
import javax.json.JsonObject;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author Keanu Stückrad
 */
public class LoginManager {

    public static final String BASE_REST_URL = "https://rbsg.uniks.de/api";

    public static User onLogin(@NonNull final User user) throws ExecutionException, InterruptedException, JSONException {

        // JsonNode vom Response des Servers erstellen
        JsonObject body = Json.createObjectBuilder()
                .add("name", user.getName())
                .add("password", user.getPassword())
                .build();
        BaseRequest request = Unirest.post(BASE_REST_URL + "/user/login").body(body.toString());
        Future<HttpResponse<JsonNode>> future = request.asJsonAsync();
        HttpResponse<JsonNode> response = future.get();
        JsonNode jsonNode = response.getBody();

        // userKey im User Clone speichern
        JSONObject data = jsonNode.getObject().getJSONObject("data");
        String userKey = data.getString("userKey");
        User userClone = new User(user, userKey);

        return userClone;

    }

}
