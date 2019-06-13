package de.uniks.se19.team_g.project_rbsg.army_builder;

import de.uniks.se19.team_g.project_rbsg.ViewComponent;
import de.uniks.se19.team_g.project_rbsg.army_builder.unit_detail.UnitDetailController;
import de.uniks.se19.team_g.project_rbsg.army_builder.unit_property_info.UnitPropertyInfoListBuilder;
import de.uniks.se19.team_g.project_rbsg.army_builder.unit_selection.UnitListEntryFactory;
import de.uniks.se19.team_g.project_rbsg.configuration.ButtonIconsSetter;
import de.uniks.se19.team_g.project_rbsg.model.Unit;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.GetUnitTypesService;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.UnitType;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * @author Goatfryed
 * @author Keanu Stückrad
 */
@Component
public class SceneController extends ButtonIconsSetter implements Initializable {

    private static final int ICON_SIZE = 40;

    public Parent root;

    private final ArmyBuilderState state;
    private final UnitListEntryFactory unitCellFactory;
    private final GetUnitTypesService getUnitTypesService;
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
    public SimpleBooleanProperty infoFlag;

    public SceneController(
        ArmyBuilderState state,
        UnitListEntryFactory unitCellFactory,
        GetUnitTypesService getUnitTypesService
    ) {
        this.state = state;
        this.unitCellFactory = unitCellFactory;
        this.getUnitTypesService = getUnitTypesService;
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

        setButtonIcons(showInfoButton, "/assets/icons/navigation/info-black.png", "/assets/icons/navigation/info-white.png", ICON_SIZE);
        infoFlag = new SimpleBooleanProperty(false);

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

    public void showInfo(ActionEvent actionEvent) {
        if(infoView == null) {
            infoView = unitPropertyInfoListBuilder.buildInfoView();
            infoView.visibleProperty().bind(infoFlag);
        }
        infoFlag.set(true);
    }

}
