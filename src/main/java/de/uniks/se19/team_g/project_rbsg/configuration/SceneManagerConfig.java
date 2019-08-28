package de.uniks.se19.team_g.project_rbsg.configuration;

import de.uniks.se19.team_g.project_rbsg.army_builder.ArmyBuilderController;
import de.uniks.se19.team_g.project_rbsg.ingame.IngameRootController;
import de.uniks.se19.team_g.project_rbsg.lobby.core.ui.LobbyViewController;
import de.uniks.se19.team_g.project_rbsg.login.StartViewController;
import de.uniks.se19.team_g.project_rbsg.scene.ViewComponent;
import javafx.fxml.FXMLLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;

/**
 * @author Jan Müller
 */
@Configuration
public class SceneManagerConfig {

    @Bean
    @Scope("prototype")
    public ViewComponent<StartViewController> loginScene(@NonNull final FXMLLoader fxmlLoader) {
        fxmlLoader.setLocation(getClass().getResource("/ui/login/startView.fxml"));
        return ViewComponent.fromLoader(fxmlLoader);
    }

    @Bean
    @Scope("prototype")
    public ViewComponent<LobbyViewController> lobbyScene(@NonNull final FXMLLoader fxmlLoader) {
        fxmlLoader.setLocation(getClass().getResource("/ui/lobby/core/lobbyView.fxml"));
        return ViewComponent.fromLoader(fxmlLoader);
    }

    @Bean
    @Scope("prototype")
    public ViewComponent<ArmyBuilderController> armyScene(@NonNull final FXMLLoader fxmlLoader)
    {
        fxmlLoader.setLocation(getClass().getResource("/ui/army_builder/armyBuilderScene.fxml"));
        return ViewComponent.fromLoader(fxmlLoader);
    }

    @Bean
    @Scope("prototype")
    public ViewComponent<IngameRootController> ingameScene(@NonNull final FXMLLoader fxmlLoader) {
        fxmlLoader.setLocation(getClass().getResource("/ui/ingame/ingameRootView.fxml"));
        return ViewComponent.fromLoader(fxmlLoader);
    }
}
