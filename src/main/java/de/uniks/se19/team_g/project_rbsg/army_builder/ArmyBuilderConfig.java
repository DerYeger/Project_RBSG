package de.uniks.se19.team_g.project_rbsg.army_builder;

import de.uniks.se19.team_g.project_rbsg.ViewComponent;
import de.uniks.se19.team_g.project_rbsg.army_builder.army.ArmyDetailController;
import de.uniks.se19.team_g.project_rbsg.army_builder.unit_detail.CanAttackTileController;
import de.uniks.se19.team_g.project_rbsg.army_builder.unit_detail.UnitDetailController;
import de.uniks.se19.team_g.project_rbsg.army_builder.unit_detail.UnitPropertyController;
import de.uniks.se19.team_g.project_rbsg.model.Unit;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

import java.io.IOException;
import java.util.function.Function;
import java.util.function.Supplier;

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
    @Scope("prototype")
    public ViewComponent<CanAttackTileController> canAttackTile(FXMLLoader fxmlLoader) {
        fxmlLoader.setLocation(getClass().getResource("/ui/army_builder/unit_info/CanAttackTile.fxml"));
        return ViewComponent.fromLoader(fxmlLoader);
    }

    @Bean
    public Supplier<ViewComponent<CanAttackTileController>> canAttackTileFactory(ObjectFactory<FXMLLoader> fxmLoader)
    {
        return () -> canAttackTile(fxmLoader.getObject());
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
