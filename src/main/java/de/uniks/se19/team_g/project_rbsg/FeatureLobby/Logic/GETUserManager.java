package de.uniks.se19.team_g.project_rbsg.FeatureLobby.Logic;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.uniks.se19.team_g.project_rbsg.FeatureLobby.Logic.Contract.DataClasses.Player;
import de.uniks.se19.team_g.project_rbsg.FeatureLobby.Logic.Contract.DataClasses.User;
import de.uniks.se19.team_g.project_rbsg.FeatureLobby.Logic.Contract.IGETUserManager;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Georg Siebert
 */

public class GETUserManager implements IGETUserManager
{
    private static final String baseURL = "https://rbsg.uniks.de/api";

    private static final String specificURL = "/user";

    private RestTemplate restTemplate;

    private HttpHeaders headers;

    private HttpStatus httpStatus;

    private User user;

    private ObjectMapper objectMapper;

    public GETUserManager(@NotNull User user)
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
        httpStatus = responseEntity.getStatusCode();
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
            JsonNode jsonNode = objectMapper.readTree(responseBodyContent);
            String status = jsonNode.get("status").asText();
            if (status.equals("success"))
            {
                return objectMapper.readValue(jsonNode.get("data").asText(), new TypeReference<ArrayList<String>>()
                {
                });
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

