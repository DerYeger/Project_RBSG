package de.uniks.se19.team_g.project_rbsg.FeatureLobbyTests;

import de.uniks.se19.team_g.project_rbsg.FeatureLobby.Logic.Contract.DataClasses.Player;
import de.uniks.se19.team_g.project_rbsg.FeatureLobby.UI.Views.LobbyViewBuilder;
import de.uniks.se19.team_g.project_rbsg.FeatureLobby.UI.Views.LobbyViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import static org.junit.Assert.*;

/**
 * @author Georg Siebert
 */

public class LobbyViewTests extends ApplicationTest
{
    private FXMLLoader fxmlLoader;

    @Override
    public void start (Stage stage) throws Exception {
        fxmlLoader = new FXMLLoader(LobbyViewController.class.getResource("LobbyView.fxml"));
        Parent mainNode = fxmlLoader.load();
        stage.setScene(new Scene(mainNode));
        stage.show();
        stage.toFront();
    }

    @Test
    public void testView() {
        Label lobbyTitle = lookup("#lobbyTitle").query();
        ListView<Player>  playerListView = lookup("#lobbyPlayerListView").query();
        assertEquals("Advanced WASP Wars", lobbyTitle.getText());
        assertNotNull(playerListView);
    }

    @Test
    public void addPlayerToList() {
        LobbyViewController lobbyViewController = fxmlLoader.getController();
        assertNotNull(lobbyViewController);
        lobbyViewController.getViewModel().getPlayerObservableCollection().addAll(new Player("Tobias"), new Player("Juri"), new Player("Omar"), new Player("Jan"), new Player("Keanu"), new Player("Christoph"), new Player("Georg"));

//        try
//        {
//            Thread.sleep(5000);
//        }
//        catch (InterruptedException e)
//        {
//            e.printStackTrace();
//        }
    }

    @Test
    public void TestBuilder() {
        LobbyViewBuilder lobbyViewBuilder = new LobbyViewBuilder();
        try
        {
            Node node = lobbyViewBuilder.buildLobbyScene();
            assertNotNull(node);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
