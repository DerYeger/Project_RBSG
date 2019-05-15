package de.uniks.se19.team_g.project_rbsg.FeatureLobbyTests;

import de.uniks.se19.team_g.project_rbsg.FeatureLobby.UI.ViewModels.Contract.DataClasses.Player;
import de.uniks.se19.team_g.project_rbsg.FeatureLobby.UI.ViewModels.LobbyViewModel;
import de.uniks.se19.team_g.project_rbsg.FeatureLobby.UI.Views.LobbyViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import static org.junit.Assert.*;

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

    @After
    public void tearDown() throws Exception {
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
        lobbyViewController.getViewModel().getPlayerObservableCollection().addAll(new Player("Georg"), new Player("Juri"), new Player("Omar"));
        try
        {
            Thread.sleep(5000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
