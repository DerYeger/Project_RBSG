package de.uniks.se19.team_g.project_rbsg.configuration;

import javafx.fxml.FXMLLoader;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class FXMLLoaderFactory {
    private final ApplicationContext context;

    public FXMLLoaderFactory(ApplicationContext context) {
        this.context = context;
    }

    public FXMLLoader createLoader() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(context::getBean);
        return fxmlLoader;
    }
}