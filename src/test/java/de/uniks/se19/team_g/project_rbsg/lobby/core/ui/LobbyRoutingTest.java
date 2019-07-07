package de.uniks.se19.team_g.project_rbsg.lobby.core.ui;


import io.rincl.Rincl;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import java.io.IOException;
import java.util.Locale;

import static org.mockito.Mockito.*;

/**
 * @author Goatfryed
 */
public class LobbyRoutingTest extends ApplicationTest {

    boolean armyRoutingCalled = false;

    @Override
    public void start(Stage stage) {
        Rincl.setLocale(Locale.ENGLISH);

        final FXMLLoader loader = new FXMLLoader();
        loader.setLocation(LobbyViewController.class.getResource("/ui/lobby/core/lobbyView.fxml"));
        loader.setControllerFactory(param -> {
            Assert.assertEquals(param, LobbyViewController.class);
            final LobbyViewController mock = mock(LobbyViewController.class);
            doAnswer(invocation -> {
                armyRoutingCalled = true;
                return null;
            }).when(mock).goToArmyBuilder(any());

            return mock;
        });

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        final Scene scene = new Scene(loader.getRoot(),1200 ,840);

        stage.setScene(scene);
        stage.show();
        stage.toFront();
    }


    @Override
    public void stop() throws Exception
    {
        FxToolkit.hideStage();
    }

    @Test
    public void testArmyRouting() throws IOException {
        Button button = lookup(".button").lookup("Armies").query();
        clickOn(button);
        Assert.assertTrue(armyRoutingCalled);
    }
}
