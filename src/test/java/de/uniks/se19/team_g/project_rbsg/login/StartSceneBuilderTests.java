package de.uniks.se19.team_g.project_rbsg.login;

import de.uniks.se19.team_g.project_rbsg.MusicManager;
import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.server.rest.LoginManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.RegistrationManager;
import io.rincl.Rincl;
import io.rincl.resourcebundle.ResourceBundleResourceI18nConcern;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        StartSceneBuilderTests.ContextConfiguration.class,
        LoginFormBuilder.class,
        LoginFormController.class,
        SplashImageBuilder.class,
        StartViewBuilder.class,
        StartViewController.class,
        UserProvider.class,
        LoginManager.class,
        RegistrationManager.class,
        SceneManager.class,
        TitleViewBuilder.class,
        TitleViewController.class,
        MusicManager.class
})
public class StartSceneBuilderTests extends ApplicationTest {

    @TestConfiguration
    static class ContextConfiguration implements ApplicationContextAware {

        private ApplicationContext context;

        @Bean
        @Scope("prototype")
        public FXMLLoader fxmlLoader()
        {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setControllerFactory(this.context::getBean);
            return fxmlLoader;
        }

        @Bean
        public RestTemplateBuilder restTemplateBuilder(){
            return new RestTemplateBuilder();
        }

        @Bean
        public RestTemplate restTemplate(){
            return new RestTemplate(getClientHttpRequestFactory());
        }

        private ClientHttpRequestFactory getClientHttpRequestFactory() {
            int timeOut = 10000;
            HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
            clientHttpRequestFactory.setConnectTimeout(timeOut);
            clientHttpRequestFactory.setReadTimeout(timeOut);
            return clientHttpRequestFactory;
        }

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

            this.context = applicationContext;
        }
    }

    @Autowired
    private ApplicationContext context;

    @Test
    public void testGetStartScene() throws IOException {
        Rincl.setDefaultResourceI18nConcern(new ResourceBundleResourceI18nConcern());
        final StartViewBuilder startViewBuilder = context.getBean(StartViewBuilder.class);
        final Scene scene = new StartSceneBuilder(startViewBuilder).getStartScene();
        WaitForAsyncUtils.waitForFxEvents();

        Assert.assertNotNull(scene);
        Assert.assertNotNull(scene.getRoot());
        Assert.assertEquals(scene.getRoot(), startViewBuilder.getStartView());

    }
}
