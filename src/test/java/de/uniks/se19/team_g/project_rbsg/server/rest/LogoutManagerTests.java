package de.uniks.se19.team_g.project_rbsg.server.rest;

import de.uniks.se19.team_g.project_rbsg.model.User;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.server.websocket.WebSocketConfigurator;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class LogoutManagerTests {

    @Test
    public void testSuccessfulLogout() {
        final UserProvider userProvider = new UserProvider();
        final RESTClient restClient = new RESTClient(new RestTemplate()) {
            @Override
            public String get(final @NonNull String endpoint, final @Nullable MultiValueMap<String, String> headers) {
                return "{\"status\":\"success\"}";
            }
        };
        final LogoutManager logoutManager = new LogoutManager(userProvider, restClient);

        final User user = new User();

        userProvider.set(user).get()
                .setName("TestUser")
                .setUserKey("1234");

        logoutManager.logout();

        Assert.assertNotEquals(user, userProvider.get());

        Assert.assertNull(WebSocketConfigurator.userKey);
    }
}
