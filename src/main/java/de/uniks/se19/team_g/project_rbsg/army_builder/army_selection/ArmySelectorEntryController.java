package de.uniks.se19.team_g.project_rbsg.army_builder.army_selection;

import de.uniks.se19.team_g.project_rbsg.configuration.ApplicationState;
import de.uniks.se19.team_g.project_rbsg.model.Army;
import de.uniks.se19.team_g.project_rbsg.util.JavaFXUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import javax.annotation.Nullable;
import java.net.URL;
import java.util.ResourceBundle;

public class ArmySelectorEntryController extends ListCell<Army> implements Initializable {

    public static final String DUMMY_ICON_PATH = ArmySelectorEntryController.class.getResource("/assets/icons/army/dragon-head.white.png").toString();

    public ImageView imageView;

    private SimpleStringProperty dummyIcon = new SimpleStringProperty(DUMMY_ICON_PATH);

    @Nullable
    private final ApplicationState appState;

    public ArmySelectorEntryController(
        @Nullable ApplicationState appState
    ) {
        this.appState = appState;
    }

    @Override
    protected void updateItem(Army item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            return;
        }

        JavaFXUtils.bindImage(
            imageView.imageProperty(),
            dummyIcon
        );
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setText(null);
        setGraphic(null);
    }

    public void onItemClicked(MouseEvent mouseEvent) {
        if (appState == null) {
            return;
        }
        appState.selectedArmy.set(getItem());
    }
}
