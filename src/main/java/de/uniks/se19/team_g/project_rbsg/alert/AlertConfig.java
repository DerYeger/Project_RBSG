package de.uniks.se19.team_g.project_rbsg.alert;

import de.uniks.se19.team_g.project_rbsg.ViewComponent;
import javafx.fxml.FXMLLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;

@Configuration
public class AlertConfig {
    @Bean
    @Scope("prototype")
    public ViewComponent<ExitAlertController> exitAlert(@NonNull final FXMLLoader fxmlLoader) {
        fxmlLoader.setLocation(getClass().getResource("/ui/alert/confirmationAlert.fxml"));
        fxmlLoader.setController(new ExitAlertController());
        return ViewComponent.fromLoader(fxmlLoader);
    }
}
