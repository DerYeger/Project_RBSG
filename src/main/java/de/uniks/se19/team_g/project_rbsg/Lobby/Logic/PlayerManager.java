package de.uniks.se19.team_g.project_rbsg.Lobby.Logic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.uniks.se19.team_g.project_rbsg.Lobby.CrossCutting.DataClasses.Player;
import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.Contract.IPlayerManager;
import de.uniks.se19.team_g.project_rbsg.model.User;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

@Component
public class PlayerManager implements IPlayerManager
{
    private static final String endpoint = "/game";

    private final RESTClient restClient;
    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    public PlayerManager(RESTClient restClient) {
        this.restClient = restClient;
        this.user = null;
    }

    @Override
    public Collection<Player> getPlayers()
    {
        if(user == null) {
            return new ArrayList<>();
        }
        LinkedMultiValueMap<String, String> headers  = new LinkedMultiValueMap<>();
        headers.add("userKey", user.getUserKey());
        String response = restClient.get(endpoint, headers);
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
            if(!players.isEmpty()) {
                return players;
            }
        }
        players.add(new Player(user.getName()));
        return players;
    }
}
