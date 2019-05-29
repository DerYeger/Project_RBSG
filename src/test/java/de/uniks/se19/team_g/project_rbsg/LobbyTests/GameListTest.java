package de.uniks.se19.team_g.project_rbsg.LobbyTests;

import de.uniks.se19.team_g.project_rbsg.JavaConfig;
import de.uniks.se19.team_g.project_rbsg.Lobby.CrossCutting.DataClasses.Game;
import de.uniks.se19.team_g.project_rbsg.Lobby.CrossCutting.DataClasses.Lobby;
import de.uniks.se19.team_g.project_rbsg.Lobby.CrossCutting.DataClasses.Player;
import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.*;
import de.uniks.se19.team_g.project_rbsg.Lobby.UI.Views.LobbyViewBuilder;
import de.uniks.se19.team_g.project_rbsg.Lobby.UI.Views.LobbyViewController;
import de.uniks.se19.team_g.project_rbsg.chat.controller.ChatController;
import de.uniks.se19.team_g.project_rbsg.chat.controller.ChatWebSocketCallback;
import de.uniks.se19.team_g.project_rbsg.chat.view.ChatBuilder;
import de.uniks.se19.team_g.project_rbsg.chat.view.ChatChannelBuilder;
import de.uniks.se19.team_g.project_rbsg.chat.view.ChatTabBuilder;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * @author Georg Siebert
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        JavaConfig.class,
        LobbyViewBuilder.class,
        LobbyViewController.class,
        GameListTest.ContextConfiguration.class,
        ChatBuilder.class,
})
public class GameListTest extends ApplicationTest
{
    @Autowired
    private ApplicationContext context;


    @Override
    public void start(Stage stage) {
        LobbyViewBuilder lobbyViewBuilder = context.getBean(LobbyViewBuilder.class);

        final Scene scene = new Scene((Parent) lobbyViewBuilder.buildLobbyScene(), 1280, 720);

        stage.setScene(scene);
        stage.show();
        stage.toFront();
    }

    @Override
    public void stop() throws Exception {
        FxToolkit.hideStage();
    }

    @TestConfiguration
    public static class ContextConfiguration {
        @Bean
        public GameManager gameManager() {
            return new GameManager(new RESTClient(new RestTemplate()), new UserProvider()) {
                @Override
                public Collection<Game> getGames() {
                    ArrayList<Game> games = new ArrayList<>();
                    games.add(new Game("1", "GameOfHello", 4, 2 ));
                    games.add(new Game("2", "DefenceOfTheAncient", 10, 7));
                    return games;
                }
            };
        }

        @Bean
        public PlayerManager playerManager() {
            return new PlayerManager(new RESTClient(new RestTemplate()), new UserProvider()) {
                @Override
                public Collection<Player> getPlayers() {
                    return new ArrayList<Player>();
                }
            };
        }

        @Bean
        public SystemMessageManager systemMessageManager() {
            return new SystemMessageManager(new WebSocketClient()) {
                @Override
                public void startSocket() {
                }
            };
        }

        @Bean
        public ChatController chatController() {
            return new ChatController(new UserProvider(), new WebSocketClient(), new ChatWebSocketCallback()) {
                @Override
                public void init(@NonNull final TabPane chatPane)
                {
                }

            };
        }
    }


    @Test
    public void addItemsAndRemove() {
        ListView<Game> gamesListView = lookup("#lobbyGamesListView").queryListView();
        assertNotNull(gamesListView);

        ObservableList<Game> games = gamesListView.getItems();
        assertNotNull(games);
        assertEquals(2, games.size());

        ListCell<Game> gameOfHello = lookup("#gameCellGameOfHello").query();
        ListCell<Game> gameOfDota = lookup("#gameCellDefenceOfTheAncient").query();

        assertNotNull(gameOfHello);
        assertNotNull(gameOfDota);

        Game goH = gameOfHello.getItem();
        assertEquals("GameOfHello", goH.getName());
        assertEquals("1", goH.getId());
        assertEquals(4, goH.getNeededPlayer());
        assertEquals(2, goH.getJoinedPlayer());

        Game dota = gameOfDota.getItem();
        assertEquals("DefenceOfTheAncient", dota.getName());
        assertEquals("2", dota.getId());
        assertEquals(10, dota.getNeededPlayer());
        assertEquals(7, dota.getJoinedPlayer());

        clickOn("#joinGameButtonGameOfHello");
        clickOn("#joinGameButtonDefenceOfTheAncient");

        Lobby lobby = context.getBean(LobbyViewController.class).getLobby();

        lobby.addGame(new Game("3", "StarWars", 2, 2));
        sleep(500);

        assertEquals(3, games.size());

        ListCell<Game> gameStarWars = lookup("#gameCellStarWars").query();
        assertNotNull(gameStarWars);

        Game starWars = gameStarWars.getItem();
        assertEquals("StarWars", starWars.getName());
        assertEquals("3", starWars.getId());
        assertEquals(2, starWars.getNeededPlayer());
        assertEquals(2, starWars.getJoinedPlayer());

        clickOn("#joinGameButtonStarWars");


        lobby.removeGame(dota);
        sleep(500);

        assertEquals(2, games.size());
    }

}
