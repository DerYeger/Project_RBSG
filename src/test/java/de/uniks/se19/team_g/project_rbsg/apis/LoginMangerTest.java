package de.uniks.se19.team_g.project_rbsg.apis;

import com.mashape.unirest.http.JsonNode;
import de.uniks.se19.team_g.project_rbsg.model.User;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

/**
 * @author Keanu Stückrad
 */
public class LoginMangerTest {

    @Test
    public void loginTest() throws ExecutionException, InterruptedException, JSONException {

        User user = new User("MasterChief", "john-117");
        JsonNode json = LoginManager.onLogin(user);
        Assert.assertEquals(json.getObject().getString("status"), "success");
        Assert.assertNotNull(user.getUserKey());

    }

}
