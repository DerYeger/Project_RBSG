package de.uniks.se19.team_g.project_rbsg.army_builder.army_selection;

import de.uniks.se19.team_g.project_rbsg.model.Army;
import io.rincl.Rincled;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.layout.HBox;
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

    public Label armiesLabel;

    public HBox header;

    @SuppressWarnings("FieldCanBeLocal")
    @Nullable
    private ListChangeListener<? super Army> fixSelectionOnSelectedRemoved;

    public ArmySelectorController(
            ArmySelectorCellFactory cellFactory

    ) {
        this.cellFactory = cellFactory;
    }

    public void setSelection(ObservableList<Army> armies, Property<Army> selection)
    {
        listView.setItems(armies);
        final MultipleSelectionModel<Army> selectionModel = listView.getSelectionModel();
        selectionModel.select(selection.getValue());

        fixSelectionOnSelectedRemoved = (ListChangeListener<Army>) c -> {
            final Army selectedItem = selectionModel.getSelectedItem();
            while (c.next()) {
                if (c.getRemoved().contains(selectedItem)) {
                    selectionModel.clearSelection();
                }
            }
        };

        armies.addListener(new WeakListChangeListener<>(fixSelectionOnSelectedRemoved));

        selectionModel.selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    selection.setValue(newValue);
                }
        );
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
