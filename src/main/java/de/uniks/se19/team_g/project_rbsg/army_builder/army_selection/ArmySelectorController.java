package de.uniks.se19.team_g.project_rbsg.army_builder.army_selection;

import de.uniks.se19.team_g.project_rbsg.model.Army;
import de.uniks.se19.team_g.project_rbsg.util.JavaFXUtils;
import io.rincl.Rincled;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.net.URL;
import java.util.ResourceBundle;

@Component
@Scope("prototype")
public class ArmySelectorController implements Initializable, Rincled {

    public ListView<Army> listView;

    private final ArmySelectorCellFactory cellFactory;
    public Button editArmyButton;
    public Label armiesLabel;

    /*
        save a reference to method reference and provide WeakChangeListener as Listener
     */
    @SuppressWarnings("FieldCanBeLocal")
    @Nullable
    private ChangeListener<Army> onSelectionChanged;

    @SuppressWarnings("FieldCanBeLocal")
    @Nullable
    private ListChangeListener<? super Army> fixSelectionOnSelectedRemoved;
    private Army lastSelectedArmy;

    public ArmySelectorController(
            ArmySelectorCellFactory cellFactory

    ) {
        this.cellFactory = cellFactory;
    }

    public void setSelection(ObservableList<Army> armies, Property<Army> selection)
    {
        listView.setItems(armies);

        fixSelectionOnSelectedRemoved = (ListChangeListener<Army>) c -> {
            while (c.next()) {
                if (c.getRemoved().contains(lastSelectedArmy)) {
                    listView.getSelectionModel().clearSelection();
                }
            }
        };

        armies.addListener(new WeakListChangeListener<>(fixSelectionOnSelectedRemoved));

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
        lastSelectedArmy = next;
        listView.getSelectionModel().select(next);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateLabel();
        listView.setCellFactory(cellFactory);
    }

    private void updateLabel() {
        armiesLabel.setText(getResources().getString("army"));
    }
}
