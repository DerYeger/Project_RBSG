package de.uniks.se19.team_g.project_rbsg.lobby.system;

import de.uniks.se19.team_g.project_rbsg.lobby.core.SystemMessageHandler.*;
import de.uniks.se19.team_g.project_rbsg.model.Game;
import de.uniks.se19.team_g.project_rbsg.lobby.model.Lobby;
import de.uniks.se19.team_g.project_rbsg.lobby.model.Player;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import static org.junit.Assert.*;

/**
 * @author Georg Siebert
 */

public class SystemMessageHandlerTest extends ApplicationTest
{
    private final String userJoinedMessage = "{\"action\":\"userJoined\",\"data\":{\"name\":\"hello2\"}}";
    private final String userLeftMessage = "{\"action\":\"userLeft\",\"data\":{\"name\":\"hello2\"}}";
    private final String gameCreatedMessage = "{\"action\":\"gameCreated\",\"data\":{\"id\":\"5cedd127fded8d00016bc5f0\",\"name\":\"testTeamBGame\",\"neededPlayer\":2}}";
    private final String gameDeletedMessage = "{\"action\":\"gameDeleted\",\"data\":{\"id\":\"1\"}}";
    private final String playerJoinedGameMessage = "{\"action\":\"playerJoinedGame\",\"data\":{\"id\":\"1\",\"joinedPlayer\":1}}";
    private final String playerLeftGameMessage = "{\"action\":\"playerLeftGame\",\"data\":{\"id\":\"1\",\"joinedPlayer\":0}}";

    @Test
    public void testDefaultSystemMessageHandler() {

        DefaultSystemMessageHandler defaultHandler = new DefaultSystemMessageHandler();

        defaultHandler.handleSystemMessage("Hallo");
    }

    @Test
    public void testUserJoinedMessageHandler() {
        Lobby lobby = new Lobby();
        UserJoinedMessageHandler userJoinedMessageHandler = new UserJoinedMessageHandler(lobby);

        assertNotNull(lobby.getPlayers());

        userJoinedMessageHandler.handleSystemMessage(userJoinedMessage);

        sleep(500);

        assertEquals(1, lobby.getPlayers().size());
        assertEquals("hello2" , lobby.getPlayers().get(0).getName());
    }

    @Test
    public void testUserLeftMessageHandler() {
        Lobby lobby = new Lobby();
        lobby.addPlayer(new Player("hello2"));

        sleep(500);

        assertEquals(1, lobby.getPlayers().size());

        UserLeftMessageHandler userLeftMessageHandler = new UserLeftMessageHandler(lobby);

        userLeftMessageHandler.handleSystemMessage(userLeftMessage);

        sleep(500);

        assertEquals(0, lobby.getPlayers().size());
    }

    @Test
    public void testGameCreatedMessageHandler() {
        Lobby lobby = new Lobby();
        lobby.addPlayer(new Player("hello2"));

        sleep(500);

        assertEquals(0, lobby.getGames().size());

        GameCreatedMessageHandler gameCreatedMessageHandler= new GameCreatedMessageHandler(lobby);

        gameCreatedMessageHandler.handleSystemMessage(gameCreatedMessage);

        sleep(500);

        assertEquals(1, lobby.getGames().size());
        Game game = lobby.getGames().get(0);
        assertEquals("testTeamBGame", game.getName());
        assertEquals("5cedd127fded8d00016bc5f0", game.getId());
        assertEquals(2 , game.getNeededPlayer());
    }

    @Test
    public void testGameDeletedMessageHandler() {
        Lobby lobby = new Lobby();
        lobby.addGame(new Game("1", "GameOfHello", 4, 0));

        sleep(500);

        assertEquals(1, lobby.getGames().size());

        GameDeletedMessageHandler gameDeletedMessageHandler= new GameDeletedMessageHandler(lobby);

        gameDeletedMessageHandler.handleSystemMessage(gameDeletedMessage);

        sleep(500);

        assertEquals(0, lobby.getGames().size());
    }

    @Test
    public void testplayerJoinedGameMessageHandler() {
        Lobby lobby = new Lobby();
        lobby.addGame(new Game("1", "GameOfHello", 4, 0));

        sleep(500);

        assertEquals(1, lobby.getGames().size());

        PlayerJoinedAndLeftGameMessageHandler playerJoinedAndLeftGameMessageHandler= new PlayerJoinedAndLeftGameMessageHandler(lobby);

        playerJoinedAndLeftGameMessageHandler.handleSystemMessage(playerJoinedGameMessage);

        sleep(500);

        assertEquals(1, lobby.getGameOverId("1").getJoinedPlayer());
    }

    @Test
    public void testplayerLeftGameMessageHandler() {
        Lobby lobby = new Lobby();
        lobby.addGame(new Game("1", "GameOfHello", 4, 1));

        sleep(500);

        assertEquals(1, lobby.getGames().size());

        PlayerJoinedAndLeftGameMessageHandler playerJoinedAndLeftGameMessageHandler= new PlayerJoinedAndLeftGameMessageHandler(lobby);

        playerJoinedAndLeftGameMessageHandler.handleSystemMessage(playerLeftGameMessage);

        sleep(500);

        assertEquals(0, lobby.getGameOverId("1").getJoinedPlayer());
    }
}
