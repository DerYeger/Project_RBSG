package de.uniks.se19.team_g.project_rbsg.lobby.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.uniks.se19.team_g.project_rbsg.lobby.model.Player;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.server.rest.RESTClient;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Georg Siebert
 */

@Component
public class PlayerManager implements IPlayerManager
{
    private static final String endpoint = "/user";

    private final RESTClient restClient;
    private final UserProvider userProvider;



    public PlayerManager(RESTClient restClient, UserProvider userProvider) {
        this.restClient = restClient;
        this.userProvider = userProvider;
    }

    @Override
    public Collection<Player> getPlayers()
    {
        if(userProvider.get() == null || userProvider.get().getUserKey() == null || userProvider.get().getUserKey() == "") {
            return new ArrayList<>();
        }
        LinkedMultiValueMap<String, String> headers  = new LinkedMultiValueMap<>();
        headers.add("userKey", userProvider.get().getUserKey());
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
        players.add(new Player(userProvider.get().getName()));
        return players;
    }
}
