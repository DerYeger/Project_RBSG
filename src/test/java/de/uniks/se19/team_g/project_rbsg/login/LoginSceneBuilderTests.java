package de.uniks.se19.team_g.project_rbsg.login;

import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.configuration.JavaConfig;
import de.uniks.se19.team_g.project_rbsg.ingame.IngameSceneBuilder;
import de.uniks.se19.team_g.project_rbsg.ingame.IngameViewBuilder;
import de.uniks.se19.team_g.project_rbsg.ingame.IngameViewController;
import de.uniks.se19.team_g.project_rbsg.lobby.core.LobbySceneBuilder;
import de.uniks.se19.team_g.project_rbsg.lobby.core.ui.LobbyViewBuilder;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.model.GameProvider;
import de.uniks.se19.team_g.project_rbsg.server.rest.LoginManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.RegistrationManager;
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
@ContextConfiguration(classes = {
        JavaConfig.class,
        LoginFormBuilder.class,
        LoginFormController.class,
        RegistrationManager.class,
        SplashImageBuilder.class,
        LoginSceneBuilder.class,
        SceneManager.class,
        LobbySceneBuilder.class,
        LobbyViewBuilder.class,
        LoginManager.class,
        RegistrationManager.class,
        UserProvider.class,
        TitleFormBuilder.class,
        TitleFormController.class,
        IngameSceneBuilder.class,
        IngameViewBuilder.class,
        IngameViewController.class,
        GameProvider.class,
        UserProvider.class
})
public class LoginSceneBuilderTests extends ApplicationTest {

    @Autowired
    private ApplicationContext context;

    @Test
    public void testGetLoginScene() throws IOException {
        final LoginFormBuilder loginFormBuilder = context.getBean(LoginFormBuilder.class);
        final TitleFormBuilder titleFormBuilder = context.getBean(TitleFormBuilder.class);
        final Scene scene = new LoginSceneBuilder(new SplashImageBuilder(), loginFormBuilder, titleFormBuilder).getLoginScene();
        Assert.assertNotNull(scene);
        Assert.assertNotNull(scene.getRoot());
        Assert.assertTrue(scene.getRoot().getChildrenUnmodifiable().contains(loginFormBuilder.getLoginForm()));
        Assert.assertTrue(scene.getRoot().getChildrenUnmodifiable().contains(titleFormBuilder.getTitleForm()));
    }
}
