package de.uniks.se19.team_g.project_rbsg.view;

import javafx.scene.Node;
import org.junit.Assert;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import java.io.IOException;

public class LoginFormBuilderTests extends ApplicationTest {

    @Test
    public void testGetLoginForm() throws IOException {
        final Node loginForm = new LoginFormBuilder().getLoginForm();
        Assert.assertNotNull(loginForm);
    }
}
