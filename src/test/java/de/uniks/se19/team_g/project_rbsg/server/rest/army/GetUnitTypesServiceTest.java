package de.uniks.se19.team_g.project_rbsg.server.rest.army;

import de.uniks.se19.team_g.project_rbsg.model.Unit;
import de.uniks.se19.team_g.project_rbsg.model.User;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.server.ServerConfig;
import de.uniks.se19.team_g.project_rbsg.server.rest.LoginManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.config.UserKeyInterceptor;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.ExecutionException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        ServerConfig.class,
        LoginManager.class,
        UserProvider.class,
        GetUnitTypesService.class,
        UserKeyInterceptor.class,
})
public class GetUnitTypesServiceTest {

    @Autowired
    public LoginManager loginManager;
    @Autowired
    public RestTemplate rbsgTemplate;
    @Autowired
    public UserProvider userProvider;
    @Autowired
    public GetUnitTypesService service;

    @Test
    @Ignore
    public void queryUnitTypesOnline() throws ExecutionException, InterruptedException {
        User user = new User("ggEngineering", "ggEngineering");
        user.setUserKey(
            loginManager.onLogin(user).get().getBody().get("data").get("userKey").asText()
        );
        userProvider.set(user);

        final List<Unit> unitTypes = service.queryUnitPrototypes().get();

        Assert.assertTrue(unitTypes.size() > 0);
    }
}