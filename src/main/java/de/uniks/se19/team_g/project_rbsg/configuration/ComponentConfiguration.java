package de.uniks.se19.team_g.project_rbsg.configuration;

import de.uniks.se19.team_g.project_rbsg.lobby.core.NotificationModalController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.function.Function;

@Configuration
public class ComponentConfiguration {

    @Bean
    public Function<VBox, NotificationModalController> notificationRenderer(ObjectFactory<FXMLLoader> fxmlLoader)
    {
        return vBox -> {
            final FXMLLoader loader = fxmlLoader.getObject();
            loader.setRoot(vBox);
            loader.setLocation(getClass().getResource("/ui/lobby/NotificationModal.fxml"));
            try {
                loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return loader.getController();
        };
    }
}
