package de.uniks.se19.team_g.project_rbsg.Lobby.Logic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.Contract.DataClasses.Player;
import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.Contract.DataClasses.User;
import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.Contract.IGETUserManager;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Georg Siebert
 */

//TODO: Auslagern der REST Anfrage in eingene Klasse + eigene Klasse für Deserializierung der Response (Handler?)

public class GetUsersRESTManager implements IGETUserManager
{
    private static final String baseURL = "https://rbsg.uniks.de/api";

    private static final String specificURL = "/user";

    private RestTemplate restTemplate;

    private HttpHeaders headers;

    private User user;

    private ObjectMapper objectMapper;

    public GetUsersRESTManager(@NotNull User user)
    {
        this.user = user;

        this.restTemplate = new RestTemplate();
        this.headers = new HttpHeaders();
        headers.add("userKey", user.getUserKey());

        objectMapper = new ObjectMapper();
    }

    @Override
    public Collection<Player> getUsers()
    {
        HttpEntity<String> requestEntity = new HttpEntity<>("", headers);
        System.out.println(requestEntity.toString());
        ResponseEntity<String> responseEntity = restTemplate
                .exchange(baseURL + specificURL, HttpMethod.GET, requestEntity, String.class);
        HttpStatus httpStatus = responseEntity.getStatusCode();
        if (httpStatus.is2xxSuccessful())
        {
            ArrayList<String> playerNames = deserializeResponse(responseEntity.getBody());
            return createPlayerList(playerNames);
        }
        return new ArrayList<Player>()
        {{
            add(new Player(user.getUsername()));
        }};
    }

    private ArrayList<String> deserializeResponse(String responseBodyContent)
    {
        try
        {
            final JsonNode jsonNode = objectMapper.readTree(responseBodyContent);
            final String status = jsonNode.get("status").asText();
            if (status.equals("success"))
            {
                final JsonNode arrNode = jsonNode.get("data");
                ArrayList<String> playerNames = null;
                if (arrNode.isArray())
                {
                    playerNames = new ArrayList<String>();
                    for (final JsonNode nameNode : arrNode)
                    {
                        playerNames.add(nameNode.asText());
                    }
                }

                return playerNames;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<Player> createPlayerList(Collection<String> playerNames)
    {
        ArrayList<Player> players = new ArrayList<Player>();
        if (playerNames != null)
        {
            for (String name : playerNames)
            {
                players.add(new Player(name));
            }
            return players;
        }
        players.add(new Player(user.getUsername()));
        return players;
    }
}

