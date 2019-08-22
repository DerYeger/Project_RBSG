package de.uniks.se19.team_g.project_rbsg.ingame.waiting_room;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

public class PlayerCardBuilderTest extends ApplicationTest {

    @Test
    public void setPlayer() {
        final PlayerCardBuilder sut = new PlayerCardBuilder();
        final Node root = sut.buildPlayerCard(null);

        WaitForAsyncUtils.waitForAsyncFx(
                500,
                () -> {
                    Stage stage = new Stage();
                    stage.setScene(new Scene((Parent) root));
                    stage.show();
                }
        );

        final Player player = new Player("1");
        player.setIsReady(false);

        Assert.assertFalse(root.getStyleClass().contains("ready"));

        clickOn("#botButton");

        sut.setPlayer(player, null);
        Assert.assertFalse(root.getStyleClass().contains("ready"));

        player.setIsReady(true);
        Assert.assertTrue(root.getStyleClass().contains("ready"));

        player.setIsReady(false);
        Assert.assertFalse(root.getStyleClass().contains("ready"));
    }
}