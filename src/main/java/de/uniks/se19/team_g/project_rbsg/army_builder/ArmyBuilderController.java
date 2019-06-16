package de.uniks.se19.team_g.project_rbsg.army_builder;

import de.uniks.se19.team_g.project_rbsg.MusicManager;
import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.ViewComponent;
import de.uniks.se19.team_g.project_rbsg.army_builder.unit_detail.UnitDetailController;
import de.uniks.se19.team_g.project_rbsg.army_builder.unit_selection.UnitListEntryFactory;
import de.uniks.se19.team_g.project_rbsg.model.Unit;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.units.GetUnitTypesService;
import de.uniks.se19.team_g.project_rbsg.util.JavaFXUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Goatfryed
 * @author Keanu Stückrad
 */
@Component
public class ArmyBuilderController implements Initializable {

    private static final int ICON_SIZE = 40;

    public Parent root;

    @Nonnull private final ArmyBuilderState state;
    @Nonnull private final UnitListEntryFactory unitCellFactory;
    @Nullable private final GetUnitTypesService getUnitTypesService;
    @Nullable private final MusicManager musicManager;
    @Nullable private final SceneManager sceneManager;

    public VBox content;
    public HBox topContentContainer;
    public ListView<Unit> unitListView;
    public Pane unitDetailView;
    public VBox armyView;
    public VBox sideBarRight;
    public VBox sideBarLeft;
    private ObjectFactory<ViewComponent<UnitDetailController>> unitDetailViewFactory;
    public Button soundButton;
    public Button leaveButton;


    public ArmyBuilderController(
            @Nonnull final ArmyBuilderState state,
            @Nonnull UnitListEntryFactory unitCellFactory,
            @Nullable GetUnitTypesService getUnitTypesService,
            @Nullable final MusicManager musicManager,
            @Nullable final SceneManager sceneManager
        ) {
        this.state = state;
        this.unitCellFactory = unitCellFactory;
        this.getUnitTypesService = getUnitTypesService;
        this.musicManager = musicManager;
        this.sceneManager = sceneManager;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        final ViewComponent<UnitDetailController> viewComponent = unitDetailViewFactory.getObject();
        unitDetailView.getChildren().add(viewComponent.getRoot());

        unitListView.setCellFactory(unitCellFactory);
        unitListView.setItems(state.unitTypes);

        if (getUnitTypesService != null) {
            getUnitTypesService.queryUnitPrototypes().thenAccept(
                unitTypes -> Platform.runLater(() ->
                    state.unitTypes.setAll(unitTypes)
                )
            );
        }

        if (musicManager != null) {
            musicManager.initButtonIcons(soundButton);
        }

        JavaFXUtils.setButtonIcons(
                leaveButton,
                getClass().getResource("/assets/icons/navigation/arrowBackWhite.png"),
                getClass().getResource("/assets/icons/navigation/arrowBackBlack.png"),
                ArmyBuilderController.ICON_SIZE
        );

    }

    public void toggleSound(ActionEvent actionEvent) {
        if(musicManager == null) return;
        musicManager.updateMusicButtonIcons(soundButton);
    }

    public void leaveRoom(ActionEvent actionEvent) {
        if (sceneManager == null) {
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Leave ArmyBuilder");
        alert.setHeaderText("Are you sure you want to exit?");
        alert.showAndWait();
        if (alert.getResult().equals(ButtonType.OK)) {
            sceneManager.setLobbyScene();
        } else {
            actionEvent.consume();
        }
    }

}
