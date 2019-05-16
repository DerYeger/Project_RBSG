package de.uniks.se19.team_g.project_rbsg.view;

import de.uniks.se19.team_g.project_rbsg.JavaConfig;
import de.uniks.se19.team_g.project_rbsg.apis.RegistrationManager;
import de.uniks.se19.team_g.project_rbsg.controller.LoginFormController;
import de.uniks.se19.team_g.project_rbsg.view.LoginFormBuilder;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testfx.framework.junit.ApplicationTest;

import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JavaConfig.class, RegistrationManager.class, LoginFormController.class, LoginFormBuilder.class})
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
