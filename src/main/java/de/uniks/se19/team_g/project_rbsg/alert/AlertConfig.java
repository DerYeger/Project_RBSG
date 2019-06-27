package de.uniks.se19.team_g.project_rbsg.alert;

import de.uniks.se19.team_g.project_rbsg.ViewComponent;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.server.rest.LogoutManager;
import javafx.fxml.FXMLLoader;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;

/**
 * @author Jan Müller
 */
@Configuration
public class AlertConfig implements ApplicationContextAware {

    private ApplicationContext context;

    @Override
    public void setApplicationContext(@NonNull final ApplicationContext context) throws BeansException {
        this.context = context;
    }

    @Bean
    @Scope("prototype")
    public ViewComponent<ExitAlertController> exitAlert(@NonNull final FXMLLoader fxmlLoader) {
        fxmlLoader.setLocation(getClass().getResource("/ui/alert/confirmationAlert.fxml"));
        fxmlLoader.setController(new ExitAlertController());
        return ViewComponent.fromLoader(fxmlLoader);
    }

    @Bean
    @Scope("prototype")
    public ViewComponent<ExitAlertController> logoutAlert(@NonNull final FXMLLoader fxmlLoader) {
        fxmlLoader.setLocation(getClass().getResource("/ui/alert/confirmationAlert.fxml"));
        fxmlLoader.setController(
                new LogoutAlertController(
                        context.getBean(UserProvider.class),
                        context.getBean(LogoutManager.class)));
        return ViewComponent.fromLoader(fxmlLoader);
    }


}
