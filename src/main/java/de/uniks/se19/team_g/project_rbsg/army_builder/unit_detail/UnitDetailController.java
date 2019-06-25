package de.uniks.se19.team_g.project_rbsg.army_builder.unit_detail;

import de.uniks.se19.team_g.project_rbsg.ViewComponent;
import de.uniks.se19.team_g.project_rbsg.army_builder.ArmyBuilderState;
import de.uniks.se19.team_g.project_rbsg.configuration.ApplicationState;
import de.uniks.se19.team_g.project_rbsg.model.Unit;
import de.uniks.se19.team_g.project_rbsg.util.JavaFXUtils;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.URL;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
public class UnitDetailController implements Initializable {

    public final static String ATTACK_ICON_URL = UnitDetailController.class.getResource("/assets/icons/army/magicDefense.png").toString();

    public StackPane imageStackPane;
    public ImageView imageView;
    public TilePane statsContainer;
    public TextArea unitDescription;
    public GridPane canAttackGrid;
    public Label canAttackLabel;

    @Nonnull
    private final Property<Locale> selectedLocale;
    @Nonnull
    private final ApplicationState appState;
    @Nonnull
    private final ArmyBuilderState state;
    @Nonnull
    private final Supplier<ViewComponent<CanAttackTileController>> canAttackTileFactory;
    @Nullable
    private ObjectFactory<ViewComponent<UnitPropertyController>> propertyViewComponentFactory;

    private ChangeListener<Unit> listener;

    public UnitDetailController(
        @Nullable ObjectFactory<ViewComponent<UnitPropertyController>> propertyViewComponentFactory,
        @Nonnull Property<Locale> selectedLocale,
        @Nonnull ApplicationState appState,
        @Nonnull ArmyBuilderState sceneState,
        @Nonnull Supplier<ViewComponent<CanAttackTileController>> canAttackTileFactory
    ) {
        this.propertyViewComponentFactory = propertyViewComponentFactory;
        this.selectedLocale = selectedLocale;
        this.appState = appState;
        this.state = sceneState;
        this.canAttackTileFactory = canAttackTileFactory;
    }



    private void bind(@Nullable Unit unit)
    {
        if (unit == null) {
            unbind();
            return;
        }

        unitDescription.textProperty().bind(unit.description);

        JavaFXUtils.bindImage(imageView.imageProperty(), unit.imageUrl);

        if (propertyViewComponentFactory != null) {
            statsContainer.getChildren().clear();
            addPropertyDetail(unit.health, new Image(UnitDetailController.ATTACK_ICON_URL));
            addPropertyDetail(unit.speed, new Image(UnitDetailController.ATTACK_ICON_URL));
        }

    }

    private void unbind() {
        unitDescription.textProperty().unbind();
        unitDescription.setText(null);
        imageView.imageProperty().unbind();
        imageView.setImage(null);
        statsContainer.getChildren().clear();
        if (propertyViewComponentFactory != null) {
            addPropertyDetail(null, new Image(UnitDetailController.ATTACK_ICON_URL));
            addPropertyDetail(null, new Image(UnitDetailController.ATTACK_ICON_URL));
        }
    }

    private void addPropertyDetail(SimpleIntegerProperty property, Image icon) {
        assert propertyViewComponentFactory != null;
        ViewComponent<UnitPropertyController> viewComponent;
        viewComponent = propertyViewComponentFactory.getObject();
        viewComponent.getController().bindTo(property, icon);
        final Node node = viewComponent.getRoot();
        statsContainer.getChildren().add(node);
        TilePane.setMargin(node, new Insets(10, 10, 0, 10));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listener = (observable, oldValue, newValue) -> bind(newValue);
        state.selectedUnit.addListener(new WeakChangeListener<>(listener));
        bind(state.selectedUnit.get());

        canAttackLabel.textProperty().bind(
            JavaFXUtils.bindTranslation(selectedLocale, "UnitCanAttack")
        );

        setupCanAttackBoxes();

    }

    private void setupCanAttackBoxes() {
        final int unitCount = appState.unitDefinitions.size();
        final int columnCount = unitCount - (unitCount / 2);

        ArrayList<ColumnConstraints> columns = new ArrayList<>();
        for (int i = 0; i < columnCount; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setHgrow(Priority.ALWAYS);
            column.setHalignment(HPos.CENTER);
            column.setMinWidth(GridPane.USE_COMPUTED_SIZE);
            column.setPrefWidth(GridPane.USE_COMPUTED_SIZE);
            column.setMaxWidth(Double.MAX_VALUE);

            columns.add(column);
        }

        canAttackGrid.getColumnConstraints().setAll(columns);

        Map<String, ViewComponent<CanAttackTileController>> canAttackNodes = new HashMap<>();

        int i = 0;
        for (Unit unitDefinition : appState.unitDefinitions) {
            int row = (i % 2);
            int column = (i / 2);

            final ViewComponent<CanAttackTileController> component = canAttackTileFactory.get();
            component.getController().setDefinition(unitDefinition);
            final Node node = component.getRoot();
            GridPane.setColumnIndex(node, column);
            GridPane.setRowIndex(node, row);
            GridPane.setMargin(node, new Insets(5));

            canAttackNodes.put(unitDefinition.id.get(), component);

            i++;
        }

        canAttackGrid.getChildren().setAll(
                canAttackNodes.values().stream()
                .<Node>map(ViewComponent::getRoot)
                .collect(Collectors.toList())
        );
    }
}
