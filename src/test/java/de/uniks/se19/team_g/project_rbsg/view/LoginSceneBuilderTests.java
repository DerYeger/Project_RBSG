package de.uniks.se19.team_g.project_rbsg.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import org.junit.Assert;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import java.io.IOException;

public class LoginSceneBuilderTests extends ApplicationTest {

    @Test
    public void testGetLoginScene() throws IOException {
        final LoginFormBuilder loginFormBuilder = new LoginFormBuilder(new FXMLLoader());
        final Scene scene = new LoginSceneBuilder(new SplashImageBuilder(), loginFormBuilder).getLoginScene();
        Assert.assertNotNull(scene);
        Assert.assertNotNull(scene.getRoot());
        Assert.assertTrue(scene.getRoot().getChildrenUnmodifiable().contains(loginFormBuilder.getLoginForm()));
    }
}
