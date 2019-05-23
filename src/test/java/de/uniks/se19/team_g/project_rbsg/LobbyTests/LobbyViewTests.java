package de.uniks.se19.team_g.project_rbsg.LobbyTests;

import de.uniks.se19.team_g.project_rbsg.JavaConfig;
import de.uniks.se19.team_g.project_rbsg.Lobby.CrossCutting.DataClasses.Player;
import de.uniks.se19.team_g.project_rbsg.Lobby.UI.Views.LobbyViewBuilder;
import de.uniks.se19.team_g.project_rbsg.Lobby.UI.Views.LobbyViewController;
import de.uniks.se19.team_g.project_rbsg.apis.RegistrationManager;
import de.uniks.se19.team_g.project_rbsg.controller.LoginFormController;
import de.uniks.se19.team_g.project_rbsg.view.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testfx.framework.junit.ApplicationTest;
import static org.junit.Assert.*;

/**
 * @author Georg Siebert
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JavaConfig.class, LoginFormBuilder.class, LoginFormController.class, RegistrationManager.class, SplashImageBuilder.class, LoginSceneBuilder.class, SceneManager.class, LobbySceneBuilder.class, LobbyViewBuilder.class})
public class LobbyViewTests extends ApplicationTest
{
//    private FXMLLoader fxmlLoader;
//
//    @Override
//    public void start (Stage stage) throws Exception {
//        fxmlLoader = new FXMLLoader(LobbyViewController.class.getResource("LobbyView.fxml"));
//        Parent mainNode = fxmlLoader.load();
//        stage.setScene(new Scene(mainNode, 600, 400));
//        stage.show();
//        stage.toFront();
//    }
//
//    @Test
//    public void testView() {
//        Label lobbyTitle = lookup("#lobbyTitle").query();
//        ListView<Player>  playerListView = lookup("#lobbyPlayerListView").query();
//        assertEquals("Advanced WASP Wars", lobbyTitle.getText());
//        assertNotNull(playerListView);
//    }
//
//    @Test
//    public void addPlayerToList() {
//        LobbyViewController lobbyViewController = fxmlLoader.getController();
//        assertNotNull(lobbyViewController);
////        lobbyViewController.getViewModel().getPlayerObservableCollection().addAll(new Player("Tobias"), new Player("Juri"), new Player("Omar"), new Player("Jan"), new Player("Keanu"), new Player("Christoph"), new Player("Georg"));
//        sleep(2000);
//    }
//
//    @Test
//    public void TestBuilder() {
//        LobbyViewBuilder lobbyViewBuilder = new LobbyViewBuilder();
//        try
//        {
//            Node node = lobbyViewBuilder.buildLobbyScene();
//            assertNotNull(node);
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
}
