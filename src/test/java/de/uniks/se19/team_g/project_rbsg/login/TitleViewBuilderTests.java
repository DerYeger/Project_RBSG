package de.uniks.se19.team_g.project_rbsg.login;

import de.uniks.se19.team_g.project_rbsg.configuration.JavaConfig;
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

/**
 * @author  Keanu Stückrad
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        JavaConfig.class,
        TitleViewBuilder.class,
        TitleViewController.class
})
public class TitleViewBuilderTests extends ApplicationTest {

    @Autowired
    private ApplicationContext context;

    @Test
    public void testGetTitleForm() throws IOException {
        final Node titleForm = context.getBean(TitleViewBuilder.class).getTitleForm();
        Assert.assertNotNull(titleForm);
    }
}

