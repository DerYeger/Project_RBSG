package de.uniks.se19.team_g.project_rbsg.Lobby.Logic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.Contract.DataClasses.Player;
import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.Contract.IPlayerManager;
import de.uniks.se19.team_g.project_rbsg.model.User;
import org.springframework.lang.NonNull;
import org.springframework.util.LinkedMultiValueMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class PlayerManager implements IPlayerManager
{
    private static final String endpoint = "/game";

    private final RESTClient restClient;
    private User user;

    public PlayerManager(final @NonNull User user) {
        restClient = new RESTClient();
        this.user = user;
    }

    @Override
    public Collection<Player> getPlayers()
    {
        LinkedMultiValueMap<String, String> headers  = new LinkedMultiValueMap<>();
        headers.add("userKey", user.getUserKey());
        String response = restClient.get(endpoint, headers, null);
        return deserialize(response);
    }

    private Collection<Player> deserialize(final String response)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseNode = null;
        ArrayList<Player> players = new ArrayList<Player>();
        try
        {
            responseNode = objectMapper.readTree(response);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        if(responseNode != null && responseNode.get("status").asText().equals("success")) {
            JsonNode playerArrayNode = responseNode.get("data");
            if (playerArrayNode.isArray()) {
                for (JsonNode playerNode : playerArrayNode)
                {
                    players.add(new Player(playerNode.asText()));
                }
            }
            return players;
        }
        players.add(new Player(user.getName()));
        return players;
    }
}
