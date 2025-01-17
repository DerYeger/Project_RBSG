package de.uniks.se19.team_g.project_rbsg.army_builder;

import de.uniks.se19.team_g.project_rbsg.scene.ViewComponent;
import de.uniks.se19.team_g.project_rbsg.army_builder.army.ArmyDetailController;
import de.uniks.se19.team_g.project_rbsg.army_builder.edit_army.EditArmyController;
import de.uniks.se19.team_g.project_rbsg.army_builder.unit_detail.CanAttackTileController;
import de.uniks.se19.team_g.project_rbsg.army_builder.unit_detail.UnitDetailController;
import de.uniks.se19.team_g.project_rbsg.army_builder.unit_detail.UnitPropertyController;
import de.uniks.se19.team_g.project_rbsg.configuration.ApplicationState;
import de.uniks.se19.team_g.project_rbsg.model.Army;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

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
        fxmlLoader.setLocation(getClass().getResource("/ui/army_builder/unit_info/canAttackTile.fxml"));
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
                loader.setLocation(getClass().getResource("/ui/army_builder/armyDetail.fxml"));
                return ViewComponent.fromLoader(loader);
        };
    }

    @Bean
    public ArmyBuilderState armyBuilderState(ApplicationState appState)
    {
        final ArmyBuilderState state = new ArmyBuilderState();

        final ObservableList<Army> dirtyAwareArmies = FXCollections.observableArrayList(
                army -> new Observable[] {army.hasUnsavedUpdates}
        );
        Bindings.bindContent(dirtyAwareArmies, appState.armies);

        state.unsavedUpdates.bind(
            Bindings.createBooleanBinding(
                () -> (dirtyAwareArmies.stream().anyMatch(Army::hasUnsavedUpdates) || state.isNumberOfArmiesChanged()),
                dirtyAwareArmies, state.numberOfArmiesChanged
            ));

        return state;
    }

    @Bean
    @Scope("prototype")
    public ViewComponent<EditArmyController> editArmyComponent(FXMLLoader fxmlLoader) {
        fxmlLoader.setLocation(getClass().getResource("/ui/army_builder/edit_army/editArmyModal.fxml"));
        return ViewComponent.fromLoader(fxmlLoader);
    }
}
