package de.uniks.se19.team_g.project_rbsg.apis;

import com.mashape.unirest.http.exceptions.UnirestException;
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
    public void loginTest() throws ExecutionException, InterruptedException, JSONException, UnirestException {
        User user = new User("MasterChief", "john-117");
        User userClone = LoginManager.onLogin(user);
        Assert.assertNotNull(userClone.getUserKey());
        Assert.assertEquals(user.getName(), userClone.getName());
        Assert.assertNull(userClone.getPassword());
    }

}
