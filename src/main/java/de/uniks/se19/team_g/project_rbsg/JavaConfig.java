package de.uniks.se19.team_g.project_rbsg.controller;

import javafx.fxml.FXMLLoader;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JavaConfig implements ApplicationContextAware {


    private ApplicationContext context;

    @Bean
    public FXMLLoader fxmlLoader(){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(this.context::getBean);
        return fxmlLoader;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        this.context = applicationContext;
    }
}
