package de.uniks.se19.team_g.project_rbsg.view;

import de.uniks.se19.team_g.project_rbsg.JavaConfig;
import de.uniks.se19.team_g.project_rbsg.apis.RegistrationManager;
import de.uniks.se19.team_g.project_rbsg.controller.LoginFormController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testfx.framework.junit.ApplicationTest;

import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JavaConfig.class, RegistrationManager.class, LoginFormController.class, LoginFormBuilder.class})
public class LoginFormBuilderTests extends ApplicationTest {

    @Test
    public void testGetLoginForm() throws IOException {
        final Node loginForm = new LoginFormBuilder(new FXMLLoader()).getLoginForm();
        Assert.assertNotNull(loginForm);
    }
}
