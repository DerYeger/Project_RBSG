package de.uniks.se19.team_g.project_rbsg.configuration;

import javafx.fxml.FXMLLoader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Configuration
public class FXMLLoaderFactory {
    private final ApplicationContext context;

    public FXMLLoaderFactory(ApplicationContext context) {
        this.context = context;
    }

    @Bean
    @Scope("prototype")
    public FXMLLoader fxmlLoader() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(context::getBean);
        return fxmlLoader;
    }
}