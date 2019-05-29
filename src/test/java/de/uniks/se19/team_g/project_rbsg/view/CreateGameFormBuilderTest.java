package de.uniks.se19.team_g.project_rbsg.view;

import de.uniks.se19.team_g.project_rbsg.JavaConfig;
import de.uniks.se19.team_g.project_rbsg.apis.GameCreator;
import de.uniks.se19.team_g.project_rbsg.controller.CreateGameController;
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
@ContextConfiguration(classes = {JavaConfig.class, CreateGameFormBuilder.class, CreateGameController.class, GameCreator.class})
public class CreateGameFormBuilderTest extends ApplicationTest {

    @Autowired
    public ApplicationContext context;

    @Test
    public void testGetCreateGameForm() throws IOException {
        final Node createGameForm = context.getBean(CreateGameFormBuilder.class).getCreateGameForm();
        Assert.assertNotNull(createGameForm);
    }
}
