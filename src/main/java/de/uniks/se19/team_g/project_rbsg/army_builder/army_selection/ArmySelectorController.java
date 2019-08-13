package de.uniks.se19.team_g.project_rbsg.army_builder.army_selection;

import de.uniks.se19.team_g.project_rbsg.model.Army;
import io.rincl.Rincl;
import io.rincl.Rincled;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

@Component
@Scope("prototype")
public class ArmySelectorController implements Initializable, Rincled {

    private final Property<Locale> selectedLocale;
    public ListView<Army> listView;

    private final ArmySelectorCellFactory cellFactory;

    public Label armiesLabel;

    public HBox header;

    @SuppressWarnings("FieldCanBeLocal")
    @Nullable
    private ListChangeListener<? super Army> fixSelectionOnSelectedRemoved;

    public ArmySelectorController(
            ArmySelectorCellFactory cellFactory,
            @Nonnull final Property<Locale> selectedLocale
    ) {
        this.cellFactory = cellFactory;
        this.selectedLocale = selectedLocale;
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
        armiesLabel.textProperty().bind(Bindings.createStringBinding(() -> Rincl.getResources(ArmySelectorController.class).getString("army"),
                        selectedLocale
                )
        );
        listView.setCellFactory(cellFactory);
    }

}
