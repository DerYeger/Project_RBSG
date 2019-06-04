package de.uniks.se19.team_g.project_rbsg.lobby.game;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.uniks.se19.team_g.project_rbsg.model.Game;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.server.rest.RESTClient;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Georg Siebert
 */

@Component
public class GameManager
{

    private final RESTClient restClient;
    private final UserProvider userProvider;

    public GameManager(@NonNull final RESTClient restClient, @NonNull final UserProvider userProvider)
    {
        this.restClient = restClient;
        this.userProvider = userProvider;
    }

    public Collection<Game> getGames()
    {
        if (userProvider.get() == null || userProvider.get().getUserKey() == null || userProvider.get().getUserKey().equals(""))
        {
            return new ArrayList<>();
        }
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("userKey", userProvider.get().getUserKey());
        String response = restClient.get("/game", headers);


        return deserialize(response);
    }

    private Collection<Game> deserialize(final String response)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<Game> games = new ArrayList<>();
        JsonNode jsonNode = null;

        try
        {
            jsonNode = objectMapper.readTree(response);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        if (jsonNode != null && jsonNode.get("status").asText().equals("success"))
        {
            JsonNode arrNode = jsonNode.get("data");
            if (arrNode.isArray())
            {
                for (JsonNode gameNode : arrNode)
                {
                    String id = gameNode.get("id").asText();
                    String name = gameNode.get("name").asText();
                    int neededPlayer = gameNode.get("neededPlayer").asInt();
                    int joinedPlayer = gameNode.get("joinedPlayer").asInt();

                    games.add(new Game(id, name, neededPlayer, joinedPlayer));
                }
            }
        }
        return games;
    }

}
