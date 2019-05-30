package de.uniks.se19.team_g.project_rbsg.lobby.Logic.SystemMessageHandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.uniks.se19.team_g.project_rbsg.lobby.CrossCutting.DataClasses.Lobby;
import de.uniks.se19.team_g.project_rbsg.lobby.CrossCutting.DataClasses.Player;
import de.uniks.se19.team_g.project_rbsg.lobby.Logic.Contract.ISystemMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import java.io.IOException;

/**
 * @author Georg Siebert
 */

public class UserJoinedMessageHandler implements ISystemMessageHandler
{
    private Logger logger = LoggerFactory.getLogger(UserJoinedMessageHandler.class);

    private final Lobby lobby;
    private ObjectMapper objectMapper;

    public UserJoinedMessageHandler(final @NonNull Lobby lobby)
    {
        this.lobby = lobby;
        objectMapper = new ObjectMapper();
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
            logger.warn("lobby is null");
        }
    }
}
