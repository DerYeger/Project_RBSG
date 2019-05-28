package de.uniks.se19.team_g.project_rbsg.Lobby.Logic.SystemMessageHandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.uniks.se19.team_g.project_rbsg.Lobby.CrossCutting.DataClasses.Lobby;
import de.uniks.se19.team_g.project_rbsg.Lobby.CrossCutting.DataClasses.Player;
import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.Contract.ISystemMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class UserLeftMessageHandler implements ISystemMessageHandler
{

    private final ObjectMapper objectMapper;
    private Lobby lobby;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public void setLobby(Lobby lobby)
    {
        this.lobby = lobby;
    }

    public UserLeftMessageHandler() {
        objectMapper = new ObjectMapper();
    }

    @Override
    public void handleSystemMessage(final @NonNull String message)
    {
        JsonNode jsonNode = null;
        try
        {
            jsonNode = objectMapper.readTree(message);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        if(jsonNode == null) {
            return;
        }

        if(!jsonNode.get("action").asText().equals("userLeft")) {
            return;
        }

        String name  = jsonNode.get("data").get("name").asText();
        if(lobby != null) {
            Player player = lobby.getPlayerByName(name);
            lobby.removePlayer(player);
        }
        else {
            logger.debug("Lobby object was null");
        }

    }

}
