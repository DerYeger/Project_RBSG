package de.uniks.se19.team_g.project_rbsg.lobby.core;

import de.uniks.se19.team_g.project_rbsg.lobby.model.Player;
import de.uniks.se19.team_g.project_rbsg.model.User;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.server.rest.RESTClient;
import org.junit.Before;
import org.junit.Test;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Georg Siebert
 */
public class PlayerManagerTest
{
    private static final String messageSuccess = "{\n" +
            "    \"status\": \"success\",\n" +
            "    \"message\": \"\",\n" +
            "    \"data\": [\n" +
            "        \"chattest2\",\n" +
            "        \"test name\",\n" +
            "        \"chattest1\",\n" +
            "        \"test1\",\n" +
            "        \"test4\",\n" +
            "        \"hello1\"\n" +
            "    ]\n" +
            "}";

    private static final String messageFailure = "{\n" +
            "    \"status\": \"failure\",\n" +
            "    \"message\": \"\",\n" +
            "    \"data\": []\n" +
            "}";

    private static final String messageSucessNoPlayers = "{\n" +
            "    \"status\": \"success\",\n" +
            "    \"message\": \"\",\n" +
            "    \"data\": []\n" +
            "}";


    private User user;

    @Before
    public void init()
    {
        user = new User("me", "hello");
        user = new User(user, "userKey");
    }

    @Test
    public void SuccessfulMessage()
    {
        PlayerManager playerManager = new PlayerManager(new RESTClient(new RestTemplate())
        {
            @Override
            public String get(final @NonNull String endpoint, final @Nullable MultiValueMap<String, String> headers)
            {
                return messageSuccess;
            }
        }, new UserProvider() {
            @Override
            public User get() {
                return user;
            }
        });


        ArrayList<Player> players = new ArrayList<>(playerManager.getPlayers());

        assertNotNull(players);
        assertEquals(6, players.size());
        assertEquals("chattest2", players.get(0).getName());
        assertEquals("test name", players.get(1).getName());
        assertEquals("chattest1", players.get(2).getName());
        assertEquals("test1", players.get(3).getName());
        assertEquals("test4", players.get(4).getName());
        assertEquals("hello1", players.get(5).getName());
    }

    @Test
    public void FailedResponse()
    {
        PlayerManager playerManager = new PlayerManager(new RESTClient(new RestTemplate())
        {
            @Override
            public String get(final @NonNull String endpoint, final @Nullable MultiValueMap<String, String> headers)
            {
                return messageFailure;
            }
        }, new UserProvider() {
            @Override
            public User get() {
                return user;
            }
        });

        ArrayList<Player> players = new ArrayList<>(playerManager.getPlayers());
        assertNotNull(players);
        assertEquals(1, players.size());
        assertEquals("me", players.get(0).getName());
    }

    @Test
    public void messageSuccessEmtpyList()
    {
        PlayerManager playerManager = new PlayerManager(new RESTClient(new RestTemplate())
        {
            @Override
            public String get(final @NonNull String endpoint, final @Nullable MultiValueMap<String, String> headers)
            {
                return messageSucessNoPlayers;
            }
        }, new UserProvider() {
            @Override
            public User get() {
                return user;
            }
        });

        ArrayList<Player> players = new ArrayList<>(playerManager.getPlayers());
        assertNotNull(players);
        assertEquals(1, players.size());
        assertEquals("me", players.get(0).getName());
    }

    @Test
    public void UserIsNull()
    {
        PlayerManager playerManager = new PlayerManager(new RESTClient(new RestTemplate()), new UserProvider() {
            @Override
            public User get() {
                return null;
            }
        });
        Collection<Player> players = playerManager.getPlayers();
        assertNotNull(players);
        assertEquals(0, players.size());
    }

}
