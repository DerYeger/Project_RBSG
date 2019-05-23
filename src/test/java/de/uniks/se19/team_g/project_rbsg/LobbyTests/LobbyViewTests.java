package de.uniks.se19.team_g.project_rbsg.LobbyTests;

import de.uniks.se19.team_g.project_rbsg.JavaConfig;
import de.uniks.se19.team_g.project_rbsg.Lobby.CrossCutting.DataClasses.Game;
import de.uniks.se19.team_g.project_rbsg.Lobby.CrossCutting.DataClasses.Player;
import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.*;
import de.uniks.se19.team_g.project_rbsg.Lobby.UI.Views.LobbyViewBuilder;
import de.uniks.se19.team_g.project_rbsg.Lobby.UI.Views.LobbyViewController;
import de.uniks.se19.team_g.project_rbsg.apis.RegistrationManager;
import de.uniks.se19.team_g.project_rbsg.controller.ChatController;
import de.uniks.se19.team_g.project_rbsg.controller.LoginFormController;
import de.uniks.se19.team_g.project_rbsg.view.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Cell;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;
import org.testfx.framework.junit.ApplicationTest;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;

/**
 * @author Georg Siebert
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        JavaConfig.class,
        LobbyViewBuilder.class,
        LobbyViewController.class,
        LobbyViewTests.ContextConfiguration.class,
        ChatBuilder.class,
        ChatController.class
})
public class LobbyViewTests extends ApplicationTest
{

    @Autowired
    ApplicationContext context;

    @Override
    public void start(final Stage stage) {
        LobbyViewBuilder lobbyViewBuilder = context.getBean(LobbyViewBuilder.class);
        final Scene scene = new Scene((Parent) lobbyViewBuilder.buildLobbyScene());

        stage.setScene(scene);
        stage.show();
        stage.toFront();
    }

    @TestConfiguration
    static class ContextConfiguration {
        @Bean
        public GameManager gameManager() {
            return new GameManager(new RESTClient(new RestTemplate())) {
                @Override
                public Collection<Game> getGames() {
                    ArrayList<Game> games = new ArrayList<>();
                    games.add(new Game("1", "GameOfHello", 4, 2 ));
                    games.add(new Game("2", "Defence of the Ancient", 10, 7));
                    return games;
                }
            };
        }

        @Bean
        public PlayerManager playerManager() {
            return new PlayerManager(new RESTClient(new RestTemplate())) {
                @Override
                public Collection<Player> getPlayers() {
                    ArrayList<Player> players = new ArrayList<>();
                    players.add(new Player("Hello"));
                    players.add(new Player("MOBAHero42"));
                    return players;
                }
            };
        }

        @Bean
        public SystemMessageManager systemMessageManager() {
            return new SystemMessageManager(new WebSocketFactory()) {
                @Override
                public void startSocket() {

                }
            };
        }
    }

    @Test
    public void checkPlayerList() {
        ListView<Player> playerListView = lookup("#lobbyPlayerListView").query();
        assertNotNull(playerListView);
        ObservableList<Player> players = playerListView.getItems();
        assertEquals(2, players.size());
        Cell<Player> cellHello = lookup("#playerCellHello").query();
        Cell<Player> cellMobaHero = lookup("#playerCellMOBAHero42").query();
        assertNotNull(cellHello);
        assertNotNull(cellMobaHero);
        clickOn("#playerCellHello");
        clickOn("#playerCellMOBAHero42");

        assertEquals("Hello", cellHello.getItem().getName());
        assertEquals("MOBAHero42", cellMobaHero.getItem().getName());
    }

}
