package de.uniks.se19.team_g.project_rbsg.army_builder;

import de.uniks.se19.team_g.project_rbsg.ViewComponent;
import de.uniks.se19.team_g.project_rbsg.army_builder.unit_detail.UnitDetailController;
import de.uniks.se19.team_g.project_rbsg.army_builder.unit_selection.UnitListEntryFactory;
import de.uniks.se19.team_g.project_rbsg.model.Unit;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.GetUnitTypesService;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.UnitType;
import javafx.application.Platform;
import javafx.fxml.Initializable;
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
 */
@Component
public class SceneController implements Initializable {

    private final ArmyBuilderState state;
    private final UnitListEntryFactory unitCellFactory;
    private final GetUnitTypesService getUnitTypesService;
    public VBox sideBarLeft;
    public VBox content;
    public HBox topContentContainer;
    public ListView<Unit> unitListView;
    public Pane unitDetailView;
    public VBox armyView;
    public VBox sideBarRight;
    public HBox armyBuilderScene;
    private ObjectFactory<ViewComponent<UnitDetailController>> unitDetailViewFactory;

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

        getUnitTypesService.queryUnitPrototypes().thenAccept(
            unitTypes -> Platform.runLater(() ->
                state.unitTypes.setAll(unitTypes)
            )
        );
    }
}
