package de.uniks.se19.team_g.project_rbsg.configuration;

import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.model.UserManager;
import de.uniks.se19.team_g.project_rbsg.termination.Terminator;
import javafx.fxml.FXMLLoader;
import org.springframework.beans.BeansException;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author Juri Lozowoj
 */

@Configuration
public class JavaConfig implements ApplicationContextAware
{

    private ApplicationContext context;

    @Bean
    @Lazy
    public Terminator terminator() {
        return new Terminator()
                .register(context.getBean(SceneManager.class))
                .register(context.getBean(UserManager.class));
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
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        this.context = applicationContext;
    }
}
