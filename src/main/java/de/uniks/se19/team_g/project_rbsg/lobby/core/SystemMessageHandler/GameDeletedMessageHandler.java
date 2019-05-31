package de.uniks.se19.team_g.project_rbsg.lobby.core.SystemMessageHandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.uniks.se19.team_g.project_rbsg.lobby.model.Game;
import de.uniks.se19.team_g.project_rbsg.lobby.model.Lobby;
import de.uniks.se19.team_g.project_rbsg.lobby.system.ISystemMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import java.io.IOException;

/**
 * @author Georg Siebert
 */
public class GameDeletedMessageHandler implements ISystemMessageHandler
{
    private Logger logger = LoggerFactory.getLogger(UserJoinedMessageHandler.class);

    private final Lobby lobby;
    private ObjectMapper objectMapper;

    public GameDeletedMessageHandler(final @NonNull Lobby lobby)
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
        if (!messageNode.get("action").asText().equals("gameDeleted"))
        {
            return;
        }

        String id = messageNode.get("data").get("id").asText();

        if (lobby != null)
        {
            Game game = lobby.getGameOverId(id);
            lobby.removeGame(game);
        }
        else
        {
            logger.warn("lobby is null");
        }
    }
}
