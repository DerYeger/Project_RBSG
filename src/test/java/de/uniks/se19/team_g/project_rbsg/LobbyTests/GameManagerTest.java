package de.uniks.se19.team_g.project_rbsg.LobbyTests;

import de.uniks.se19.team_g.project_rbsg.lobby.model.Game;
import de.uniks.se19.team_g.project_rbsg.lobby.game.GameManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.RESTClient;
import de.uniks.se19.team_g.project_rbsg.model.User;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import org.junit.Before;
import org.junit.Test;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * @author Georg Siebert
 */
public class GameManagerTest
{
    public static final String messageSuccess = "{\n" +
            "    \"status\": \"success\",\n" +
            "    \"message\": \"\",\n" +
            "    \"data\": [\n" +
            "        {\n" +
            "            \"id\": \"5ce5c8d350487200013b9be2\",\n" +
            "            \"name\": \"gameofHello\",\n" +
            "            \"neededPlayer\": 4,\n" +
            "            \"joinedPlayer\": 0\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": \"5ce5c8de50487200013b9be3\",\n" +
            "            \"name\": \"gameofHello2\",\n" +
            "            \"neededPlayer\": 4,\n" +
            "            \"joinedPlayer\": 0\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": \"5ce5c8e450487200013b9be4\",\n" +
            "            \"name\": \"gameofHello3\",\n" +
            "            \"neededPlayer\": 4,\n" +
            "            \"joinedPlayer\": 0\n" +
            "        }\n" +
            "    ]\n" +
            "}";

    public static final String messageFailure = "{\n" +
            "    \"status\": \"failure\",\n" +
            "    \"message\": \"\",\n" +
            "    \"data\": []\n" +
            "}";
    private User user;

    @Before
    public void init() {
        user = new User("me", "hallo");
        user = new User(user, "userKey");
    }

    @Test
    public void successfulResponse() {
        GameManager gameManager = new GameManager(new RESTClient(new RestTemplate()) {
            @Override
            public String get(final @NonNull String endpoint, final @Nullable MultiValueMap<String, String> headers) {
                return messageSuccess;
            }
        }, new UserProvider() {
            @Override
            public User get() {
                return user;
            }
        });

        ArrayList<Game> games = new ArrayList<>(gameManager.getGames());
        assertNotNull(games);
        assertEquals(3, games.size());
        assertEquals("5ce5c8d350487200013b9be2", games.get(0).getId());
        assertEquals("gameofHello", games.get(0).getName());
        assertEquals(4, games.get(0).getNeededPlayer());
        assertEquals(0, games.get(0).getJoinedPlayer());
        assertEquals("5ce5c8de50487200013b9be3", games.get(1).getId());
        assertEquals("5ce5c8e450487200013b9be4", games.get(2).getId());
    }

    @Test
    public void failedResponse() {
        GameManager gameManager = new GameManager(new RESTClient(new RestTemplate()) {
            @Override
            public String get(final @NonNull String endpoint, final @Nullable MultiValueMap<String, String> headers) {
                return messageFailure;
            }
        }, new UserProvider() {
            @Override
            public User get() {
                return user;
            }
        });

        ArrayList<Game> games = new ArrayList<>(gameManager.getGames());
        assertNotNull(games);
        assertEquals(0, games.size());
    }

    @Test
    public void UserIsNull() {
        GameManager gameManager = new GameManager(new RESTClient(new RestTemplate()), new UserProvider() {
            @Override
            public User get() {
                return null;
            }
        });

        ArrayList<Game> games = new ArrayList<>(gameManager.getGames());
        assertNotNull(games);
        assertEquals(0, games.size());
    }
}
