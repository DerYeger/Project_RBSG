package de.uniks.se19.team_g.project_rbsg.login;

import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.configuration.ApplicationStateInitializer;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.server.rest.LoginManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.RegistrationManager;
import io.rincl.*;
import io.rincl.resourcebundle.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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

import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        LoginFormBuilderTests.ContextConfiguration.class,
        LoginFormBuilder.class,
        LoginFormController.class,
        UserProvider.class,
        LoginManager.class,
        RegistrationManager.class,
        SceneManager.class
})
public class LoginFormBuilderTests extends ApplicationTest {

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
    public void testGetLoginForm() throws IOException {
        Rincl.setDefaultResourceI18nConcern(new ResourceBundleResourceI18nConcern());
        final Node loginForm = context.getBean(LoginFormBuilder.class).getLoginForm();
        Assert.assertNotNull(loginForm);
    }
}
