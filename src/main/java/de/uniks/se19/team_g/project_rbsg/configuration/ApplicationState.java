package de.uniks.se19.team_g.project_rbsg.configuration;

import de.uniks.se19.team_g.project_rbsg.model.Army;
import de.uniks.se19.team_g.project_rbsg.model.Unit;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Component;

/**
 * The application State in its current form is designed as a singleton accessible from everywhere in the application.
 * This makes updates across different ui components for major application concerns (e.g. current list of user armies)
 * rather easy.
 * The application state provides JavaFX properties, where we can listen for model updates and update the ui accordingly.
 * The application state should live in the JavaFX thread, thus, updates on it should be done via Platform.runLater()
 *
 * Since the application state is a singleton, it never goes out of scope until the application closes. This also means that
 * listeners will be kept until removed and ui graphs bound to thouse listeners will be kept and updated.
 *
 * In order to prevent that, one can wrap their Listeners in WeakListeners, see Observable{}.addListener, thus increasing the performance
 * and improving the memory management.
 *
 * There is no benchmark and proof of significance, and it can be ignored currently, but the effort is rather small as well.
 * If you decide to use a WeakListener, be sure, that the listener and its object graph are referenceable as long as required.
 * Be aware of the trade off.
 *
 * Please note, that if you create a Binding, e.g. via Bindings.create{}, Observable{}.bind() ..., the references are weak by default.
 * If these are bound to Scene Graph Nodes, one doesn't need to care about the listener topic at all.
 */
@Component
public class ApplicationState {

    public static final int MAX_ARMY_COUNT = 7;
    public static final int ARMY_MAX_UNIT_COUNT = 10;

    public final SimpleObjectProperty<Army> selectedArmy = new SimpleObjectProperty<>();
    public final ObservableList<Army> armies =  FXCollections.observableArrayList();
    public final ObservableList<Unit> unitDefinitions = FXCollections.observableArrayList();
    public final SimpleBooleanProperty validArmySelected = new SimpleBooleanProperty();

    public final ObservableList<String> notifications = FXCollections.observableArrayList();

    public ApplicationState () {

        setupValidArmySelected();

        armies.addListener(this::onArmyUpdate);

    }

    protected void setupValidArmySelected() {
        validArmySelected.set(false);
        selectedArmy.addListener((observable, oldValue, newValue) ->{
            if (newValue == null) {
                validArmySelected.unbind();
                validArmySelected.setValue(false);
            } else {
                validArmySelected.bind(newValue.isPlayable);
            }
        });
    }

    private void onArmyUpdate(ListChangeListener.Change<? extends Army> change) {
        while (change.next()) {
            if (selectedArmy.get() == null || change.getRemoved().contains(selectedArmy.get())) {
                final ObservableList<? extends Army> list = change.getList();
                if (!list.isEmpty()) {
                    selectedArmy.set(list.get(0));
                }
                if (selectedArmy.get() != null) {
                    break;
                }
            }
        }
    }
}