package de.uniks.se19.team_g.project_rbsg.Lobby.Logic.SystemMessageHandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.uniks.se19.team_g.project_rbsg.Lobby.CrossCutting.DataClasses.Game;
import de.uniks.se19.team_g.project_rbsg.Lobby.CrossCutting.DataClasses.Lobby;
import de.uniks.se19.team_g.project_rbsg.Lobby.CrossCutting.DataClasses.Player;
import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.Contract.ISystemMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import java.io.IOException;

/**
 * @author Georg Siebert
 */

public class GameCreatedMessageHandler implements ISystemMessageHandler
{
    private Logger logger = LoggerFactory.getLogger(UserJoinedMessageHandler.class);

    private final Lobby lobby;
    private ObjectMapper objectMapper;

    public GameCreatedMessageHandler(final @NonNull Lobby lobby)
    {
        this.lobby = lobby;
        objectMapper = new ObjectMapper();
    }



    @Override
    public void handleSystemMessage(String message)
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
        if (!messageNode.get("action").asText().equals("gameCreated"))
        {
            return;
        }

        String id = messageNode.get("data").get("id").asText();
        String name = messageNode.get("data").get("name").asText();
        int neededPlayer = messageNode.get("data").get("neededPlayer").asInt();

        if (lobby != null)
        {
            lobby.addGame(new Game(id, name, neededPlayer, 0));
        }
        else
        {
            logger.warn("Lobby is null");
        }
    }
}
