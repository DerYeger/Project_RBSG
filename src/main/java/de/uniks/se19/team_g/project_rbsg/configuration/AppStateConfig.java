package de.uniks.se19.team_g.project_rbsg.configuration;

import de.uniks.se19.team_g.project_rbsg.model.Army;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppStateConfig {

    @Bean
    public ApplicationState appState() {
        final ApplicationState state = new ApplicationState();

        configurePlayAbleArmiesBinding(state);
        configureDefaultArmySelection(state);

        return state;
    }

    protected void configurePlayAbleArmiesBinding(ApplicationState state) {
        state.hasPlayableArmies.set(false);

        // consider making armies by default publish changes on some army fields
        ObservableList<Army> armies = FXCollections.observableArrayList(
            army -> new Observable[]{
                    army.isPlayable
                }
            );
        Bindings.bindContent(armies, state.armies);

        state.hasPlayableArmies.bind(
            Bindings.createBooleanBinding(
                () -> armies.stream().anyMatch(army -> army.isPlayable.get()),
                armies
            )
        );
    }

    protected void configureDefaultArmySelection(ApplicationState state) {
        state.armies.addListener(
            (ListChangeListener<? super Army>) change -> {
                while (change.next()) {
                    if (state.selectedArmy.get() == null || change.getRemoved().contains(state.selectedArmy.get())) {
                        final ObservableList<? extends Army> list = change.getList();
                        if (!list.isEmpty()) {
                            state.selectedArmy.set(list.get(0));
                        }
                        if (state.selectedArmy.get() != null) {
                            break;
                        }
                    }
                }
            }
        );
    }
}
