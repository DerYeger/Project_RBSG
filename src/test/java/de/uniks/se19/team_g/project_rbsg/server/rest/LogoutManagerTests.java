package de.uniks.se19.team_g.project_rbsg.server.rest;

import de.uniks.se19.team_g.project_rbsg.model.User;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.server.websocket.WebSocketConfigurator;
import de.uniks.se19.team_g.project_rbsg.termination.Terminator;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * @author Jan Müller
 */
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
        final LogoutManager logoutManager = new LogoutManager(userProvider, restClient, new Terminator());

        final User user = new User();

        userProvider.set(user).get()
                .setName("TestUser")
                .setUserKey("1234");

        WebSocketConfigurator.userKey = user.getUserKey();

        logoutManager.terminate();

        Assert.assertNotEquals(user, userProvider.get());

        Assert.assertNull(WebSocketConfigurator.userKey);
    }

    @Test
    public void testUnsuccessfulLogout() {
        final UserProvider userProvider = new UserProvider();
        final RESTClient restClient = new RESTClient(new RestTemplate()) {
            @Override
            public String get(final @NonNull String endpoint, final @Nullable MultiValueMap<String, String> headers) {
                return "{\"status\":\"failure\"}"; //specific status doesn't matter, as long as it isn't "success"
            }
        };
        final LogoutManager logoutManager = new LogoutManager(userProvider, restClient, new Terminator());

        final User user = new User();

        userProvider.set(user).get()
                .setName("TestUser")
                .setUserKey("1234");

        WebSocketConfigurator.userKey = user.getUserKey();

        logoutManager.logout();

        Assert.assertEquals(user, userProvider.get());

        Assert.assertEquals("1234", WebSocketConfigurator.userKey);
    }

    @Test
    public void testUnknownResponse() {
        final UserProvider userProvider = new UserProvider();
        final RESTClient restClient = new RESTClient(new RestTemplate()) {
            @Override
            public String get(final @NonNull String endpoint, final @Nullable MultiValueMap<String, String> headers) {
                return "{ }"; //not containing field "status"
            }
        };
        final LogoutManager logoutManager = new LogoutManager(userProvider, restClient, new Terminator());

        final User user = new User();

        userProvider.set(user).get()
                .setName("TestUser")
                .setUserKey("1234");

        WebSocketConfigurator.userKey = user.getUserKey();

        logoutManager.logout();

        Assert.assertEquals(user, userProvider.get());

        Assert.assertEquals("1234", WebSocketConfigurator.userKey);
    }

    @Test
    public void testErrorParsing() {
        final UserProvider userProvider = new UserProvider();
        final RESTClient restClient = new RESTClient(new RestTemplate()) {
            @Override
            public String get(final @NonNull String endpoint, final @Nullable MultiValueMap<String, String> headers) {
                return "some gibberish";
            }
        };
        final LogoutManager logoutManager = new LogoutManager(userProvider, restClient, new Terminator());

        final User user = new User();

        userProvider.set(user).get()
                .setName("TestUser")
                .setUserKey("1234");

        WebSocketConfigurator.userKey = user.getUserKey();

        logoutManager.logout();

        Assert.assertEquals(user, userProvider.get());

        Assert.assertEquals("1234", WebSocketConfigurator.userKey);
    }

    @Test
    public void testNoUserLoggedInLogout() {
        final UserProvider userProvider = new UserProvider();
        final RESTClient restClient = new RESTClient(new RestTemplate());
        final LogoutManager logoutManager = new LogoutManager(userProvider, restClient, new Terminator());

        final User emptyUser = userProvider.get();

        WebSocketConfigurator.userKey = null;

        logoutManager.logout();

        Assert.assertEquals(emptyUser, userProvider.get());
    }
}
