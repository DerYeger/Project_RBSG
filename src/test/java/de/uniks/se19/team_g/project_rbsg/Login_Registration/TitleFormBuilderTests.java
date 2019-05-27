package de.uniks.se19.team_g.project_rbsg.Login_Registration;

import de.uniks.se19.team_g.project_rbsg.JavaConfig;
import de.uniks.se19.team_g.project_rbsg.Lobby.UI.Views.LobbyViewBuilder;
import de.uniks.se19.team_g.project_rbsg.apis.LoginManager;
import de.uniks.se19.team_g.project_rbsg.apis.RegistrationManager;
import de.uniks.se19.team_g.project_rbsg.controller.LoginFormController;
import de.uniks.se19.team_g.project_rbsg.view.*;
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
@ContextConfiguration(classes = {JavaConfig.class, LoginFormBuilder.class, LoginFormController.class, RegistrationManager.class, SplashImageBuilder.class, LoginSceneBuilder.class, SceneManager.class, LobbySceneBuilder.class, LobbyViewBuilder.class, LoginManager.class, RegistrationManager.class, TitleFormBuilder.class, TitleFormController.class})
public class TitleFormBuilderTests extends ApplicationTest {

    @Autowired
    private ApplicationContext context;

    @Test
    public void testGetTitleForm() throws IOException {
        final Node titleForm = context.getBean(TitleFormBuilder.class).getTitleForm();
        Assert.assertNotNull(titleForm);
    }
}

