package de.uniks.se19.team_g.project_rbsg;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.BaseRequest;
import org.json.JSONException;
import org.json.JSONObject;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author Keanu Stückrad
 */
public class LoginManager {

    public static final String BASE_REST_URL = "https://rbsg.uniks.de/api";

    public static JsonNode onLogin(final User user) throws ExecutionException, InterruptedException, JSONException {

        // JsonNode vom Response des Servers erstellen
        JsonObject body = Json.createObjectBuilder()
                .add("name", user.getName())
                .add("password", user.getPassword())
                .build();
        BaseRequest request = Unirest.post(BASE_REST_URL + "/user/login").body(body.toString());
        Future<HttpResponse<JsonNode>> future = request.asJsonAsync();
        HttpResponse<JsonNode> response = future.get();
        JsonNode json = response.getBody();

        // userKey im User speichern
        JSONObject obj = json.getObject();
        String data = obj.getString("data");
        JsonReader reader = Json.createReader(new StringReader(data));
        JsonObject jsonObj = reader.readObject();
        reader.close();
        String userKey = jsonObj.getString("userKey");
        user.setUserKey(userKey);

        return json;

    }

}
