package de.uniks.se19.team_g.project_rbsg.army_builder.army_selection;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.function.Function;

@Configuration
public class ArmySelectorConfig {

    @Bean
    public Function<VBox, ArmySelectorController> armySelectorComponent(ObjectFactory<FXMLLoader> fxmlLoader)
    {
        return pane -> {
            final FXMLLoader loader = fxmlLoader.getObject();
            loader.setRoot(pane);
            loader.setLocation(getClass().getResource("/ui/army_builder/armySelector.fxml"));
            try {
                loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return loader.getController();
        };
    }
}
