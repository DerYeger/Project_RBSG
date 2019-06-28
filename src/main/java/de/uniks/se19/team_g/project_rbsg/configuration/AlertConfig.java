package de.uniks.se19.team_g.project_rbsg.configuration;

import de.uniks.se19.team_g.project_rbsg.ViewComponent;
import de.uniks.se19.team_g.project_rbsg.alert.ConfirmationAlertController;
import de.uniks.se19.team_g.project_rbsg.alert.InfoAlertController;
import javafx.fxml.FXMLLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;

/**
 * @author Jan Müller
 */
@Configuration
public class AlertConfig {

    @Bean
    @Scope("prototype")
    public ViewComponent<ConfirmationAlertController> confirmationAlert(@NonNull final FXMLLoader fxmlLoader) {
        fxmlLoader.setLocation(getClass().getResource("/ui/alert/confirmationAlert.fxml"));
        return ViewComponent.fromLoader(fxmlLoader);
    }

    @Bean
    @Scope("prototype")
    public ViewComponent<InfoAlertController> infoAlert(@NonNull final FXMLLoader fxmlLoader) {
        fxmlLoader.setLocation(getClass().getResource("/ui/alert/infoAlert.fxml"));
        return ViewComponent.fromLoader(fxmlLoader);
    }
}
