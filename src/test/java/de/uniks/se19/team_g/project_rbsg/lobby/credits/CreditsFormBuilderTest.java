package de.uniks.se19.team_g.project_rbsg.lobby.credits;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testfx.framework.junit.ApplicationTest;

import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        FXMLLoader.class,
        CreditsFormBuilder.class,
        CreditsController.class,
        CreditsFormBuilderTest.ContextConfiguration.class
})
public class CreditsFormBuilderTest extends ApplicationTest {

    @TestConfiguration
    static class ContextConfiguration implements ApplicationContextAware {

        private ApplicationContext context;

        @Bean
        @Scope("prototype")
        public FXMLLoader fxmlLoader() {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setControllerFactory(this.context::getBean);
            return fxmlLoader;
        }

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.context = applicationContext;
        }
    }

    @Autowired
    public ApplicationContext context;

    @Test
    public void testCreditsForm() throws IOException {

        final Node creditsForm = context.getBean(CreditsFormBuilder.class).getCreditsForm();
        Assert.assertNotNull(creditsForm);
    }
}
