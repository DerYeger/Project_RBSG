package de.uniks.se19.team_g.project_rbsg.view;

import de.uniks.se19.team_g.project_rbsg.JavaConfig;
import de.uniks.se19.team_g.project_rbsg.apis.RegistrationManager;
import de.uniks.se19.team_g.project_rbsg.controller.LoginFormController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testfx.framework.junit.ApplicationTest;

import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JavaConfig.class, RegistrationManager.class, LoginFormController.class, LoginFormBuilder.class, SplashImageBuilder.class, LoginSceneBuilder.class})
public class LoginSceneBuilderTests extends ApplicationTest {

    @Autowired
    private ApplicationContext context;

    @Test
    public void testGetLoginScene() throws IOException {
        final LoginFormBuilder loginFormBuilder = context.getBean(LoginFormBuilder.class);
        final Scene scene = new LoginSceneBuilder(new SplashImageBuilder(), loginFormBuilder).getLoginScene();
        Assert.assertNotNull(scene);
        Assert.assertNotNull(scene.getRoot());
        Assert.assertTrue(scene.getRoot().getChildrenUnmodifiable().contains(loginFormBuilder.getLoginForm()));
    }
}
