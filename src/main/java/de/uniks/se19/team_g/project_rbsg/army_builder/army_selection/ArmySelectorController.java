package de.uniks.se19.team_g.project_rbsg.army_builder.army_selection;

import de.uniks.se19.team_g.project_rbsg.model.Army;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.net.URL;
import java.util.ResourceBundle;

@Component
@Scope("prototype")
public class ArmySelectorController implements Initializable {

    public ListView<Army> listView;

    private final ArmySelectorCellFactory cellFactory;

    /*
        save a reference to method reference and provide WeakChangeListener as Listener
     */
    @SuppressWarnings("FieldCanBeLocal")
    @Nullable
    private ChangeListener<Army> onSelectionChanged;

    public ArmySelectorController(
            ArmySelectorCellFactory cellFactory

    ) {
        this.cellFactory = cellFactory;
    }

    public void setSelection(ObservableList<Army> armies, Property<Army> selection)
    {
        listView.setItems(armies);

        if (selection != null) {
            // save a new method reference to our method so that old WeakChangeListener can be invalidated
            // don't ask me, why we need to keep strong references to the listener and the weak listener here
            // usually, the strong listener should be enough, but for reasons, it won't get fired
            onSelectionChanged = ArmySelectorController.this::onSelectionChanged;

            selection.addListener(new WeakChangeListener<>(onSelectionChanged));
            onSelectionChanged(selection, null, selection.getValue());
        } else {
            onSelectionChanged = null;
        }
    }

    private void onSelectionChanged(ObservableValue<? extends Army> observableValue, Army prev, Army next) {
        listView.getSelectionModel().select(next);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listView.setCellFactory(cellFactory);
    }
}
