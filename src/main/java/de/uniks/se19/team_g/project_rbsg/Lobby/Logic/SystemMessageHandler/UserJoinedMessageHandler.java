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

/**
 * @author Georg Siebert
 */

@Component
public class UserJoinedMessageHandler implements ISystemMessageHandler
{
    private Logger logger = LoggerFactory.getLogger(UserJoinedMessageHandler.class);

    private Lobby lobby;
    private ObjectMapper objectMapper;

    public UserJoinedMessageHandler()
    {
        objectMapper = new ObjectMapper();
    }

    public void setLobby(Lobby lobby)
    {
        this.lobby = lobby;
    }

    @Override
    public void handleSystemMessage(final @NonNull String message)
    {
        JsonNode messageNode = null;

        try
        {
            messageNode = objectMapper.readTree(message);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        if (messageNode == null)
        {
            return;
        }
        if (!messageNode.get("action").asText().equals("userJoined"))
        {
            return;
        }

        String name = messageNode.get("data").get("name").asText();
        if (lobby != null)
        {
            lobby.addPlayer(new Player(name));
        }
        else
        {
            logger.warn("Lobby is null");
        }
    }
}
