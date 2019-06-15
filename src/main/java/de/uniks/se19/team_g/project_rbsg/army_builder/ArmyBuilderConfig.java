package de.uniks.se19.team_g.project_rbsg.army_builder;

import de.uniks.se19.team_g.project_rbsg.ViewComponent;
import de.uniks.se19.team_g.project_rbsg.army_builder.unit_detail.UnitDetailController;
import de.uniks.se19.team_g.project_rbsg.army_builder.unit_detail.UnitPropertyController;
import javafx.fxml.FXMLLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

/**
 * @author Goatfryed
 */
@Configuration
@Lazy
public class ArmyBuilderConfig {

    @Bean
    @Scope("prototype")
    public ViewComponent<SceneController> armyBuilderScene(FXMLLoader fxmlLoader)
    {
        fxmlLoader.setLocation(getClass().getResource("/ui/army_builder/armyBuilderScene.fxml"));
        return ViewComponent.fromLoader(fxmlLoader);
    }

    @Bean
    @Scope("prototype")
    public ViewComponent<UnitPropertyController> unitProperty(FXMLLoader fxmlLoader) {
        fxmlLoader.setLocation(getClass().getResource("/ui/army_builder/unitPropertyView.fxml"));
        return ViewComponent.fromLoader(fxmlLoader);
    }

    @Bean
    @Scope("prototype")
    public ViewComponent<UnitDetailController> unitDetail(FXMLLoader fxmlLoader) {
        fxmlLoader.setLocation(getClass().getResource("/ui/army_builder/unitDetailView.fxml"));
        return ViewComponent.fromLoader(fxmlLoader);
    }

    @Bean
    public ArmyBuilderState armyBuilderState()
    {
        return new ArmyBuilderState();
    }
}
