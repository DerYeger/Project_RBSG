package de.uniks.se19.team_g.project_rbsg.view;

import de.uniks.se19.team_g.project_rbsg.JavaConfig;
import de.uniks.se19.team_g.project_rbsg.Lobby.UI.Views.LobbyViewBuilder;
import de.uniks.se19.team_g.project_rbsg.apis.LoginManager;
import de.uniks.se19.team_g.project_rbsg.apis.RegistrationManager;
import de.uniks.se19.team_g.project_rbsg.controller.LoginFormController;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import javafx.scene.Node;
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
        UserProvider.class})
public class LoginFormBuilderTests extends ApplicationTest {

    @Autowired
    private ApplicationContext context;

    @Test
    public void testGetLoginForm() throws IOException {
        final Node loginForm = context.getBean(LoginFormBuilder.class).getLoginForm();
        Assert.assertNotNull(loginForm);
    }
}
