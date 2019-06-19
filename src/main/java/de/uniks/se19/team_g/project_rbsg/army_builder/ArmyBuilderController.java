package de.uniks.se19.team_g.project_rbsg.army_builder;

import de.uniks.se19.team_g.project_rbsg.MusicManager;
import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.ViewComponent;
import de.uniks.se19.team_g.project_rbsg.army_builder.army.ArmyDetailController;
import de.uniks.se19.team_g.project_rbsg.army_builder.army_selection.ArmySelectorController;
import de.uniks.se19.team_g.project_rbsg.army_builder.unit_detail.UnitDetailController;
import de.uniks.se19.team_g.project_rbsg.army_builder.unit_selection.UnitListEntryFactory;
import de.uniks.se19.team_g.project_rbsg.configuration.ApplicationState;
import de.uniks.se19.team_g.project_rbsg.configuration.JavaConfig;
import de.uniks.se19.team_g.project_rbsg.model.Unit;
import de.uniks.se19.team_g.project_rbsg.util.JavaFXUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Function;

/**
 * @author Goatfryed
 * @author Keanu Stückrad
 */
@Component
@Scope("prototype")
public class ArmyBuilderController implements Initializable {

    public Parent root;

    @Nonnull
    private final ApplicationState appState;
    @Nonnull
    private final ArmyBuilderState viewState;
    @Nullable
    private final Function<HBox, ViewComponent<ArmyDetailController>> armyDetaiLFactory;
    @Nonnull
    private final UnitListEntryFactory unitCellFactory;
    @Nullable
    private final Function<Pane, ArmySelectorController> armySelectorComponent;
    @Nullable
    private final MusicManager musicManager;
    @Nullable
    private final SceneManager sceneManager;
    @Nullable
    private final ObjectFactory<ViewComponent<UnitDetailController>> unitDetailViewFactory;

    public VBox content;
    public HBox topContentContainer;
    public ListView<Unit> unitListView;
    public Pane unitDetailView;
    public HBox armyDetailsContainer;
    public VBox sideBarRight;
    public VBox sideBarLeft;
    public Button soundButton;
    public Button leaveButton;
    public Pane armySelectorRoot;
    private ChangeListener<Unit> onSelectionUpdated;


    public ArmyBuilderController(
            @Nonnull ApplicationState appState,
            @Nonnull ArmyBuilderState viewState,
            @Nonnull UnitListEntryFactory unitCellFactory,
            @Nullable ObjectFactory<ViewComponent<UnitDetailController>> unitDetailViewFactory,
            @Nullable Function<HBox, ViewComponent<ArmyDetailController>> armyDetaiLFactory,
            @Nullable Function<Pane, ArmySelectorController> armySelectorComponent,
            @Nullable MusicManager musicManager,
            @Nullable SceneManager sceneManager
    ) {
        this.appState = appState;
        this.viewState = viewState;
        this.armyDetaiLFactory = armyDetaiLFactory;
        this.unitCellFactory = unitCellFactory;
        this.armySelectorComponent = armySelectorComponent;
        this.musicManager = musicManager;
        this.sceneManager = sceneManager;
        this.unitDetailViewFactory = unitDetailViewFactory;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        unitListView.setCellFactory(unitCellFactory);
        unitListView.setItems(appState.unitDefinitions);

        // setup listener to focus items based on selected unit
        onSelectionUpdated = this::onSelectionUpdated;
        viewState.selectedUnit.addListener(new WeakChangeListener<>(onSelectionUpdated));

        if (armyDetaiLFactory != null) {
            armyDetaiLFactory.apply(armyDetailsContainer);
        }

        if (unitDetailViewFactory != null) {
            final ViewComponent<UnitDetailController> viewComponent = unitDetailViewFactory.getObject();
            unitDetailView.getChildren().add(viewComponent.getRoot());
        }

        if (musicManager != null) {
            musicManager.initButtonIcons(soundButton);
        }

        if (armySelectorComponent != null) {
            armySelectorComponent.apply(armySelectorRoot).setSelection(
                appState.armies
            );
        }

        JavaFXUtils.setButtonIcons(
                leaveButton,
                getClass().getResource("/assets/icons/navigation/arrowBackWhite.png"),
                getClass().getResource("/assets/icons/navigation/arrowBackBlack.png"),
                JavaConfig.ICON_SIZE
        );

    }

    public void onSelectionUpdated(ObservableValue<? extends Unit> observable, Unit oldValue, Unit newValue)
    {
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

    public void toggleSound(ActionEvent actionEvent) {
        if(musicManager == null) return;
        musicManager.updateMusicButtonIcons(soundButton);
    }

    public void leaveRoom(ActionEvent actionEvent) {
        if (sceneManager == null) {
            return;
        }
        sceneManager.setLobbyScene();
    }

}
