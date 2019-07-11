package de.uniks.se19.team_g.project_rbsg.ingame.waiting_room;

import de.uniks.se19.team_g.project_rbsg.ingame.waiting_room.model.Player;
import javafx.scene.Node;
import org.junit.Assert;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

public class PlayerCardBuilderTest extends ApplicationTest {

    @Test
    public void setPlayer() {

        final PlayerCardBuilder sut = new PlayerCardBuilder();
        final Node root = sut.buildPlayerCard();

        final Player player = new Player("1");
        player.setIsReady(false);

        Assert.assertFalse(root.getStyleClass().contains("ready"));
        sut.setPlayer(player, null);
        Assert.assertFalse(root.getStyleClass().contains("ready"));

        player.setIsReady(true);
        Assert.assertTrue(root.getStyleClass().contains("ready"));

        player.setIsReady(false);
        Assert.assertFalse(root.getStyleClass().contains("ready"));
    }
}