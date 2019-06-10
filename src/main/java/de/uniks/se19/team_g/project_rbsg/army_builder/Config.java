package de.uniks.se19.team_g.project_rbsg.army_builder;

import de.uniks.se19.team_g.project_rbsg.ViewComponent;
import javafx.fxml.FXMLLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class Config {

    @Bean
    @Scope("prototype")
    public ViewComponent armyBuilderScene(FXMLLoader fxmlLoader)
    {
        fxmlLoader.setLocation(SceneController.class.getResource("armyBuilderScene.fxml"));
        return ViewComponent.fromLoader(fxmlLoader);
    }
}
