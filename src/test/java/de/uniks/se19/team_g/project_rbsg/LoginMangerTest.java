package de.uniks.se19.team_g.project_rbsg;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.CompletableFuture;
import org.junit.Assert;
import org.junit.Test;

public class LoginMangerTest {

    @Test
    public void LoginTest(){

        User sentUser = new User("Keanu", "geheim");
        CompletableFuture<User> future = LoginManager.onLogin(sentUser);
        Assert.assertNotNull(future);
        ObjectMapper objMapper = new ObjectMapper();
        User receivedUser = objMapper.readValue(future., User.class);
        Assert.assertEquals(sentUser.getName(), receivedUser.getName());
        Assert.assertNotNull(sentUser.getPassword(), receivedUser.getPassword());

    }

}
