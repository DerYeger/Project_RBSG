package de.uniks.se19.team_g.project_rbsg.view;

import de.uniks.se19.team_g.project_rbsg.login.SplashImageBuilder;
import javafx.scene.layout.BackgroundImage;
import org.junit.Assert;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

public class SplashImageBuilderTest extends ApplicationTest {

    @Test
    public void testGetSplashImage() {
        final BackgroundImage backgroundImage = new SplashImageBuilder().getSplashImage();
        Assert.assertNotNull(backgroundImage);
        Assert.assertNotNull(backgroundImage.getImage());
        Assert.assertTrue(backgroundImage.getImage().getProgress() == 1 && !backgroundImage.getImage().isError());
    }
}
