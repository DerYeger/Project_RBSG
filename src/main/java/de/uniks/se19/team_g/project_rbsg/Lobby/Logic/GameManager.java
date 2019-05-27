package de.uniks.se19.team_g.project_rbsg.Lobby.Logic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.uniks.se19.team_g.project_rbsg.Lobby.CrossCutting.DataClasses.Game;
import de.uniks.se19.team_g.project_rbsg.model.User;
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
    private User user;

    public void setUser(User user)
    {
        this.user = user;
    }

    public GameManager(final RESTClient restClient)
    {
        this.restClient = restClient;
        this.user = null;
    }

    public Collection<Game> getGames()
    {
        if (user == null)
        {
            return new ArrayList<>();
        }
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("userKey", user.getUserKey());
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
