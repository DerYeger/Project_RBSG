package de.uniks.se19.team_g.project_rbsg.configuration;

import de.uniks.se19.team_g.project_rbsg.ViewComponent;
import de.uniks.se19.team_g.project_rbsg.overlay.alert.ConfirmationAlert;
import de.uniks.se19.team_g.project_rbsg.overlay.alert.InfoAlert;
import javafx.fxml.FXMLLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;

/**
 * @author Jan Müller
 */
@Configuration
public class OverlayConfiguration {

    @Bean
    @Scope("prototype")
    public ViewComponent<ConfirmationAlert> confirmationAlertView(@NonNull final FXMLLoader fxmlLoader) {
        fxmlLoader.setLocation(getClass().getResource("/ui/alert/confirmationAlert.fxml"));
        return ViewComponent.fromLoader(fxmlLoader);
    }

    @Bean
    @Scope("prototype")
    public ViewComponent<InfoAlert> infoAlertView(@NonNull final FXMLLoader fxmlLoader) {
        fxmlLoader.setLocation(getClass().getResource("/ui/alert/infoAlert.fxml"));
        return ViewComponent.fromLoader(fxmlLoader);
    }
}
