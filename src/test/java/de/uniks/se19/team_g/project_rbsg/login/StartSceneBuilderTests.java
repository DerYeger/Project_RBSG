package de.uniks.se19.team_g.project_rbsg.login;

import de.uniks.se19.team_g.project_rbsg.MusicManager;
import de.uniks.se19.team_g.project_rbsg.overlay.alert.AlertBuilder;
import de.uniks.se19.team_g.project_rbsg.scene.SceneManager;
import de.uniks.se19.team_g.project_rbsg.configuration.SceneManagerConfig;
import de.uniks.se19.team_g.project_rbsg.scene.ViewComponent;
import de.uniks.se19.team_g.project_rbsg.configuration.ApplicationStateInitializer;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.server.rest.LoginManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.LogoutManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.RegistrationManager;
import de.uniks.se19.team_g.project_rbsg.scene.RootController;
import io.rincl.Rincl;
import io.rincl.resourcebundle.ResourceBundleResourceI18nConcern;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
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

import static org.mockito.Mockito.mock;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        StartSceneBuilderTests.ContextConfiguration.class,
        LoginFormBuilder.class,
        LoginFormController.class,
        SplashImageBuilder.class,
        StartViewController.class,
        UserProvider.class,
        LoginManager.class,
        RegistrationManager.class,
        SceneManager.class,
        TitleViewBuilder.class,
        TitleViewController.class,
        MusicManager.class,
        SceneManagerConfig.class
})
public class StartSceneBuilderTests extends ApplicationTest {

    @TestConfiguration
    static class ContextConfiguration implements ApplicationContextAware {

        private ApplicationContext context;

        @Bean
        public ApplicationStateInitializer stateInitializer() {
            return Mockito.mock(ApplicationStateInitializer.class);
        }

        @Bean
        @Scope("prototype")
        public FXMLLoader fxmlLoader()
        {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setControllerFactory(this.context::getBean);
            return fxmlLoader;
        }

        @Bean
        public LogoutManager logoutManager() {
            return mock(LogoutManager.class);
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

        @Bean
        public AlertBuilder alertBuilder() {
            return mock(AlertBuilder.class);
        }

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

            this.context = applicationContext;
        }
    }

    @Autowired
    private ApplicationContext context;

    @Test
    public void testGetStartScene() {
        Rincl.setDefaultResourceI18nConcern(new ResourceBundleResourceI18nConcern());
        @SuppressWarnings("unchecked")
        final Scene scene = new Scene(((ViewComponent<RootController>) context.getBean("loginScene")).getRoot());

        WaitForAsyncUtils.waitForFxEvents();

        Assert.assertNotNull(scene);
        Assert.assertNotNull(scene.getRoot());
    }
}
