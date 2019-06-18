package de.uniks.se19.team_g.project_rbsg.army_builder;

import de.uniks.se19.team_g.project_rbsg.MusicManager;
import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.ViewComponent;
import de.uniks.se19.team_g.project_rbsg.army_builder.unit_detail.UnitDetailController;
import de.uniks.se19.team_g.project_rbsg.army_builder.unit_property_info.UnitPropertyInfoListBuilder;
import de.uniks.se19.team_g.project_rbsg.army_builder.unit_selection.UnitListEntryFactory;
import de.uniks.se19.team_g.project_rbsg.model.Unit;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.units.GetUnitTypesService;
import de.uniks.se19.team_g.project_rbsg.util.JavaFXUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Goatfryed
 * @author Keanu Stückrad
 */
@Component
public class SceneController implements Initializable {

    private static final int ICON_SIZE = 40;

    private final ArmyBuilderState state;
    private final UnitListEntryFactory unitCellFactory;
    private final GetUnitTypesService getUnitTypesService;

    public StackPane root;
    public VBox content;
    public HBox topContentContainer;
    public ListView<Unit> unitListView;
    public Pane unitDetailView;
    public VBox armyView;
    public VBox sideBarRight;
    public VBox sideBarLeft;
    private ObjectFactory<ViewComponent<UnitDetailController>> unitDetailViewFactory;
    public Button showInfoButton;
    private UnitPropertyInfoListBuilder unitPropertyInfoListBuilder;
    private Node infoView;
    public Button soundButton;
    public Button leaveButton;

    private final MusicManager musicManager;
    private final SceneManager sceneManager;

    public SceneController(
            ArmyBuilderState state,
            UnitListEntryFactory unitCellFactory,
            GetUnitTypesService getUnitTypesService,
            @NonNull final MusicManager musicManager,
            @NonNull final SceneManager sceneManager
            ) {
        this.state = state;
        this.unitCellFactory = unitCellFactory;
        this.getUnitTypesService = getUnitTypesService;
        this.musicManager = musicManager;
        this.sceneManager = sceneManager;
    }

    @Autowired
    public void setUnitDetailViewFactory(ObjectFactory<ViewComponent<UnitDetailController>> unitDetailViewFactory)
    {

        this.unitDetailViewFactory = unitDetailViewFactory;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        final ViewComponent<UnitDetailController> viewComponent = unitDetailViewFactory.getObject();
        unitDetailView.getChildren().add(viewComponent.getRoot());

        unitListView.setCellFactory(unitCellFactory);
        unitListView.setItems(state.unitTypes);

        getUnitTypesService.queryUnitPrototypes().thenAccept(
            unitTypes -> Platform.runLater(() ->
                state.unitTypes.setAll(unitTypes)
            )
        );

        unitPropertyInfoListBuilder = new UnitPropertyInfoListBuilder();
        musicManager.initButtonIcons(soundButton);

        JavaFXUtils.setButtonIcons(
                showInfoButton,
                getClass().getResource("/assets/icons/navigation/infoBlack.png"),
                getClass().getResource("/assets/icons/navigation/infoWhite.png"),
                SceneController.ICON_SIZE
        );
        JavaFXUtils.setButtonIcons(
                leaveButton,
                getClass().getResource("/assets/icons/navigation/arrowBackWhite.png"),
                getClass().getResource("/assets/icons/navigation/arrowBackBlack.png"),
                SceneController.ICON_SIZE
        );

    }

    public void toggleSound(ActionEvent actionEvent) {
        musicManager.updateMusicButtonIcons(soundButton);
    }

    public void showInfo(ActionEvent actionEvent) {
        if(infoView == null) {
            infoView = unitPropertyInfoListBuilder.buildInfoView();
            root.getChildren().add(infoView);
            StackPane.setAlignment(infoView, Pos.CENTER);
        }
        infoView.setVisible(true);
    }

    public void leaveRoom(ActionEvent actionEvent) {
        
    }
}
