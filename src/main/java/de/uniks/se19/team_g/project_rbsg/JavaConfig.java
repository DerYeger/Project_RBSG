package de.uniks.se19.team_g.project_rbsg;

import javafx.fxml.FXMLLoader;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.client.RestTemplate;

/**
 * @author Juri Lozowoj
 */

@Configuration
public class JavaConfig implements ApplicationContextAware {


    private ApplicationContext context;

    @Bean
    public FXMLLoader fxmlLoader(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(this.context::getBean);
        return fxmlLoader;
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
