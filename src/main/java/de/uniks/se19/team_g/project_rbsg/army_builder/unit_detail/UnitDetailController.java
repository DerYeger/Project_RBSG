package de.uniks.se19.team_g.project_rbsg.army_builder.unit_detail;

import de.uniks.se19.team_g.project_rbsg.ViewComponent;
import de.uniks.se19.team_g.project_rbsg.army_builder.ArmyBuilderState;
import de.uniks.se19.team_g.project_rbsg.model.Unit;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import org.springframework.beans.factory.ObjectFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.URL;
import java.util.ResourceBundle;

public class UnitDetailController implements Initializable {

    public final static String ATTACK_ICON_URL = UnitDetailController.class.getResource("/assets/icons/army/magic-defense.png").toString();
    @Nonnull private final ArmyBuilderState state;
    public StackPane imageStackPane;
    public ImageView imageView;
    public TilePane statsContainer;
    public TextArea unitDescription;
    @Nullable private ObjectFactory<ViewComponent<UnitPropertyController>> propertyViewComponentFactory;

    public UnitDetailController(
        @Nullable ObjectFactory<ViewComponent<UnitPropertyController>> propertyViewComponentFactory,
        @Nonnull  ArmyBuilderState state
    ) {
        this.propertyViewComponentFactory = propertyViewComponentFactory;
        this.state = state;
    }



    public void bind(@Nonnull Unit unit)
    {

        unitDescription.textProperty().bind(unit.description);

        final ObjectBinding<Image> imageBinding = Bindings.createObjectBinding(() -> new Image(unit.imageUrl.get()), unit.imageUrl);
        imageView.imageProperty().bind(imageBinding);

        if (propertyViewComponentFactory != null) {
            statsContainer.getChildren().clear();
            addPropertyDetail(unit.health, new Image(UnitDetailController.ATTACK_ICON_URL));
            addPropertyDetail(unit.physicalResistance, new Image(UnitDetailController.ATTACK_ICON_URL));
            addPropertyDetail(unit.magicResistance, new Image(UnitDetailController.ATTACK_ICON_URL));
            addPropertyDetail(unit.speed, new Image(UnitDetailController.ATTACK_ICON_URL));
            addPropertyDetail(unit.attack, new Image(UnitDetailController.ATTACK_ICON_URL));
            addPropertyDetail(unit.spellPower, new Image(UnitDetailController.ATTACK_ICON_URL));
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
    }
}
