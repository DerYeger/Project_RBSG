package de.uniks.se19.team_g.project_rbsg.army_builder;

import de.uniks.se19.team_g.project_rbsg.ViewComponent;
import de.uniks.se19.team_g.project_rbsg.army_builder.army.ArmyDetailController;
import de.uniks.se19.team_g.project_rbsg.army_builder.unit_detail.UnitDetailController;
import de.uniks.se19.team_g.project_rbsg.army_builder.unit_detail.UnitPropertyController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

import java.util.function.Function;

/**
 * @author Goatfryed
 */
@Configuration
@Lazy
public class ArmyBuilderConfig {

    @Bean
    @Scope("prototype")
    public ViewComponent<ArmyBuilderController> armyBuilderScene(FXMLLoader fxmlLoader)
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
    public Function<HBox,ViewComponent<ArmyDetailController>> armyDetail(ObjectFactory<FXMLLoader> fxmlLoader) {
        return box -> {
                final FXMLLoader loader = fxmlLoader.getObject();
                loader.setRoot(box);
                loader.setLocation(getClass().getResource("/ui/army_builder/ArmyDetail.fxml"));
                return ViewComponent.fromLoader(loader);
        };
    }

    @Bean
    public ArmyBuilderState armyBuilderState()
    {
        return new ArmyBuilderState();
    }
}
