package de.uniks.se19.team_g.project_rbsg.army_builder;

import de.uniks.se19.team_g.project_rbsg.MusicManager;
import de.uniks.se19.team_g.project_rbsg.RootController;
import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.ViewComponent;
import de.uniks.se19.team_g.project_rbsg.army_builder.army.ArmyDetailController;
import de.uniks.se19.team_g.project_rbsg.army_builder.army_selection.ArmySelectorController;
import de.uniks.se19.team_g.project_rbsg.army_builder.edit_army.EditArmyController;
import de.uniks.se19.team_g.project_rbsg.army_builder.unit_detail.UnitDetailController;
import de.uniks.se19.team_g.project_rbsg.army_builder.unit_property_info.UnitPropertyInfoListBuilder;
import de.uniks.se19.team_g.project_rbsg.army_builder.unit_selection.UnitListCellFactory;
import de.uniks.se19.team_g.project_rbsg.configuration.ApplicationState;
import de.uniks.se19.team_g.project_rbsg.configuration.JavaConfig;
import de.uniks.se19.team_g.project_rbsg.model.Army;
import de.uniks.se19.team_g.project_rbsg.model.Unit;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.persistance.PersistentArmyManager;
import de.uniks.se19.team_g.project_rbsg.util.JavaFXUtils;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Goatfryed
 * @author Keanu Stückrad
 * @author Jan Müller
 */
@Component
@Scope("prototype")
public class ArmyBuilderController implements Initializable, RootController {

    @Nonnull
    private final ApplicationState appState;
    @Nonnull
    private final ArmyBuilderState viewState;
    @Nonnull
    private final ViewComponent<EditArmyController> editArmyComponent;
    @Nullable
    private final Function<HBox, ViewComponent<ArmyDetailController>> armyDetaiLFactory;
    @Nonnull
    private final UnitListCellFactory unitCellFactory;
    @Nullable
    private final Function<VBox, ArmySelectorController> armySelectorComponent;
    @Nullable
    private final MusicManager musicManager;
    @Nullable
    private final SceneManager sceneManager;
    @Nullable
    private final ObjectFactory<ViewComponent<UnitDetailController>> unitDetailViewFactory;
    public StackPane root;
    public VBox content;
    public HBox topContentContainer;
    public ListView<Unit> unitListView;
    public Pane unitDetailView;
    public HBox armyDetailsContainer;
    public VBox sideBarRight;
    public VBox sideBarLeft;
    public Button soundButton;
    public Button leaveButton;
    public Button deleteArmyButton;
    public Button saveArmiesButton;
    public Button showInfoButton;
    public VBox armySelectorRoot;
    public Button editArmyButton;

    public HBox modalContainer;

    @Nonnull
    PersistentArmyManager persistantArmyManager;


    @SuppressWarnings("FieldCanBeLocal")
    private ChangeListener<Unit> onSelectionUpdated;

    private Node infoView;
    private UnitPropertyInfoListBuilder unitPropertyInfoListBuilder;

    /*
     * do NOT. i repeat. do NOT inline the army selector. We need the reference so that the selected listener won't get removed.
     */
    @SuppressWarnings("FieldCanBeLocal")
    private ArmySelectorController armySelectorController;


    public ArmyBuilderController(
            @Nonnull ApplicationState appState,
            @Nonnull ArmyBuilderState viewState,
            @Nonnull UnitListCellFactory unitCellFactory,
            @Nonnull ViewComponent<EditArmyController> editArmyComponent,
            @Nullable ObjectFactory<ViewComponent<UnitDetailController>> unitDetailViewFactory,
            @Nullable Function<HBox, ViewComponent<ArmyDetailController>> armyDetaiLFactory,
            @Nullable Function<VBox, ArmySelectorController> armySelectorComponent,
            @Nullable MusicManager musicManager,
            @Nullable SceneManager sceneManager,
            @Nonnull PersistentArmyManager persistantArmyManager
    ) {
        this.appState = appState;
        this.viewState = viewState;
        this.editArmyComponent = editArmyComponent;
        this.armyDetaiLFactory = armyDetaiLFactory;
        this.unitCellFactory = unitCellFactory;
        this.armySelectorComponent = armySelectorComponent;
        this.musicManager = musicManager;
        this.sceneManager = sceneManager;
        this.unitDetailViewFactory = unitDetailViewFactory;
        this.persistantArmyManager = persistantArmyManager;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        unitListView.setCellFactory(unitCellFactory);
        unitListView.setItems(appState.unitDefinitions);

        // setup listener to focus items based on selected unit
        onSelectionUpdated = this::onSelectionUpdated;
        viewState.selectedUnit.addListener(new WeakChangeListener<>(onSelectionUpdated));

        configureArmyDetail();
        configureUnitDetail();
        configureMusicManager();
        configureArmySelector();

        unitPropertyInfoListBuilder = new UnitPropertyInfoListBuilder();



        JavaFXUtils.setButtonIcons(
                leaveButton,
                getClass().getResource("/assets/icons/navigation/arrowBackWhite.png"),
                getClass().getResource("/assets/icons/navigation/arrowBackBlack.png"),
                JavaConfig.ICON_SIZE
        );
        JavaFXUtils.setButtonIcons(
                showInfoButton,
                getClass().getResource("/assets/icons/navigation/infoWhite.png"),
                getClass().getResource("/assets/icons/navigation/infoBlack.png"),
                JavaConfig.ICON_SIZE
        );
        JavaFXUtils.setButtonIcons(
                deleteArmyButton,
                getClass().getResource("/assets/icons/operation/deleteWhite.png"),
                getClass().getResource("/assets/icons/operation/deleteBlack.png"),
                80
        );
        JavaFXUtils.setButtonIcons(
                saveArmiesButton,
                getClass().getResource("/assets/icons/operation/saveWhite.png"),
                getClass().getResource("/assets/icons/operation/saveBlack.png"),
                80
        );
        JavaFXUtils.setButtonIcons(
                editArmyButton,
                getClass().getResource("/assets/icons/operation/editWhite.png"),
                getClass().getResource("/assets/icons/operation/editBlack.png"),
                80
        );

        saveArmiesButton.disableProperty().bind(viewState.unsavedUpdates.not());
    }

    protected void configureArmyDetail() {
        if (armyDetaiLFactory != null) {
            armyDetaiLFactory.apply(armyDetailsContainer);
        }
    }

    protected void configureUnitDetail() {
        if (unitDetailViewFactory != null) {
            final ViewComponent<UnitDetailController> viewComponent = unitDetailViewFactory.getObject();
            unitDetailView.getChildren().add(viewComponent.getRoot());
        }
    }

    protected void configureMusicManager() {
        if (musicManager != null) {
            musicManager.initButtonIcons(soundButton);
        }
    }

    protected void configureArmySelector() {
        if (armySelectorComponent == null) {
            return;
        }

        armySelectorController = armySelectorComponent.apply(armySelectorRoot);
        armySelectorController.setSelection(
                appState.armies, appState.selectedArmy
        );

        final Circle statusIndicator = new Circle(7.5d);
        statusIndicator.disableProperty().bind(viewState.unsavedUpdates.not());
        statusIndicator.getStyleClass().add("status-indicator");

        armySelectorController.header.getChildren().add( statusIndicator);
    }

    @SuppressWarnings("unused")
    public void onSelectionUpdated(ObservableValue<? extends Unit> observable, Unit oldValue, Unit newValue) {
        final Unit selection;
        if (newValue == null) {
            selection = null;
        } else {
            final String id = viewState.selectedUnit.get().id.get();
            selection = appState.unitDefinitions.stream()
                    .filter(u -> id.equals(u.id.get()))
                    .findFirst()
                    .orElse(null)
            ;
        }
        unitListView.getSelectionModel().select(selection);
    }

    public void toggleSound() {
        if (musicManager == null) {
            return;
        }
        musicManager.toggleMusicAndUpdateButtonIconSet(soundButton);
    }

    public void leaveRoom() {
        if (sceneManager == null) {
            return;
        }
        if(viewState.unsavedUpdates.get()==true){
            try {
                persistantArmyManager.saveArmies(appState.armies);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        sceneManager.setScene(SceneManager.SceneIdentifier.LOBBY, true, SceneManager.SceneIdentifier.ARMY_BUILDER);
    }

    public void saveArmies() {

        appState.armies.forEach(army -> army.setUnsavedUpdates(false));
        final List<Army> dirtyArmies = appState.armies.stream().filter(Army::hasUnsavedUpdates).collect(Collectors.toList());

        try {
            persistantArmyManager.saveArmies(appState.armies)
                // if the save doesn't work, reset the dirty flags to allow new tries
                .exceptionally(throwable -> {
                    Platform.runLater( () -> dirtyArmies.forEach(army -> army.setUnsavedUpdates(true)));
                    return null;
                })
            ;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void showInfo() {

        if (infoView == null) {
            infoView = unitPropertyInfoListBuilder.buildInfoView();
            root.getChildren().add(infoView);
            StackPane.setAlignment(infoView, Pos.CENTER);
        }
        infoView.setVisible(true);
    }

    public void deleteArmy() {
        //For clean-deletion
        Army army = appState.selectedArmy.get();
        army.setUnsavedUpdates(true);
        army.units.clear();
    }

    public void editArmy() {
        final EditArmyController controller = editArmyComponent.getController();
        controller.setOnClose(() -> modalContainer.setVisible(false));
        controller.setArmy(appState.selectedArmy.get());

        modalContainer.getChildren().setAll( editArmyComponent.<Node>getRoot());
        modalContainer.setVisible(true);
    }
}
