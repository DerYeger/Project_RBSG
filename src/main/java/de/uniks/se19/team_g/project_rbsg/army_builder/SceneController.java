package de.uniks.se19.team_g.project_rbsg.army_builder;

import de.uniks.se19.team_g.project_rbsg.MusicManager;
import de.uniks.se19.team_g.project_rbsg.ViewComponent;
import de.uniks.se19.team_g.project_rbsg.army_builder.unit_detail.UnitDetailController;
import de.uniks.se19.team_g.project_rbsg.army_builder.unit_selection.UnitListEntryFactory;
import de.uniks.se19.team_g.project_rbsg.model.Unit;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.GetUnitTypesService;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.UnitType;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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
    public VBox content;
    public HBox topContentContainer;
    public ListView<Unit> unitListView;
    public Pane unitDetailView;
    public VBox armyView;
    public VBox sideBarRight;
    public AnchorPane armyBuilderScene;
    private ObjectFactory<ViewComponent<UnitDetailController>> unitDetailViewFactory;
    public Button soundButton;

    private final MusicManager musicManager;

    public SceneController(
        ArmyBuilderState state,
        UnitListEntryFactory unitCellFactory,
        GetUnitTypesService getUnitTypesService,
        @NonNull final MusicManager musicManager
    ) {
        this.state = state;
        this.unitCellFactory = unitCellFactory;
        this.getUnitTypesService = getUnitTypesService;
        this.musicManager = musicManager;
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

        getUnitTypesService.queryUnitTypes().thenAccept(
            unitTypes -> Platform.runLater(() ->
                state.unitTypes.setAll(
                    unitTypes.stream()
                        .map(this::mapUnitTypes)
                        .collect(Collectors.toList())
                )
            )
        );

        musicManager.initButtonIcons(soundButton);

    }

    private Unit mapUnitTypes(UnitType unitType) {
        final Unit unit = new Unit();
        unit.iconUrl.set(getClass().getResource("/assets/icons/army/magic-defense.png").toString());
        unit.imageUrl.set(getClass().getResource("/assets/sprites/Soldier.png").toString());
        unit.name.set(unitType.type);
        unit.description.set(unitType.id);
        unit.speed.set(unitType.mp);
        unit.health.set(unitType.hp);
        return unit;
    }

    public void toggleSound(ActionEvent actionEvent) {
        musicManager.updateMusicButtonIcons(soundButton);
    }

}
