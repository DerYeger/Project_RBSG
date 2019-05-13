package de.uniks.se19.team_g.project_rbsg.view;

import javafx.scene.layout.BackgroundImage;
import org.junit.Assert;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

public class SplashImageBuilderTest extends ApplicationTest {

    private BackgroundImage backgroundImage;

    @Test
    public void testGetSplashImage() {
        backgroundImage = SplashImageBuilder.getSplashImage();
        Assert.assertNotNull(backgroundImage);
        Assert.assertNotNull(backgroundImage.getImage());
        Assert.assertTrue(backgroundImage.getImage().getProgress() == 1 && !backgroundImage.getImage().isError());
    }
}