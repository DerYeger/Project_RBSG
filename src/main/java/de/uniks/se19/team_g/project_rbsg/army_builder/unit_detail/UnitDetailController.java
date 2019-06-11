package de.uniks.se19.team_g.project_rbsg.army_builder.unit_detail;

import de.uniks.se19.team_g.project_rbsg.ViewComponent;
import de.uniks.se19.team_g.project_rbsg.model.Unit;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import org.springframework.beans.factory.ObjectFactory;

import javax.annotation.Nullable;

public class UnitDetailController {

    public final static Image attackIcon = new Image(UnitDetailController.class.getResource("/assets/icons/army/magic-defense.png").toString());

    public StackPane imageStackPane;
    public ImageView imageView;
    public TilePane statsContainer;
    public TextField unitDescription;
    @Nullable private ObjectFactory<ViewComponent<UnitPropertyController, Node>> propertyViewComponentFactory;

    public UnitDetailController(
        @Nullable ObjectFactory<ViewComponent<UnitPropertyController, Node>> propertyViewComponentFactory
    ) {
        this.propertyViewComponentFactory = propertyViewComponentFactory;
    }

    public void bind(Unit unit)
    {
        unitDescription.textProperty().bind(unit.description);
        updateImage(unit.imageUrl.get());
        unit.imageUrl.addListener( (observable, old, next) -> updateImage(next));

        if (propertyViewComponentFactory != null) {
            statsContainer.getChildren().clear();
            addPropertyDetail(unit.health, UnitDetailController.attackIcon);
            addPropertyDetail(unit.physicalResistance, UnitDetailController.attackIcon);
            addPropertyDetail(unit.magicResistance, UnitDetailController.attackIcon);
            addPropertyDetail(unit.speed, UnitDetailController.attackIcon);
            addPropertyDetail(unit.attack, UnitDetailController.attackIcon);
            addPropertyDetail(unit.spellPower, UnitDetailController.attackIcon);
        }

    }

    private void addPropertyDetail(SimpleIntegerProperty property, Image icon) {
        assert propertyViewComponentFactory != null;
        ViewComponent<UnitPropertyController, Node> viewComponent;
        viewComponent = propertyViewComponentFactory.getObject();
        viewComponent.getController().bindTo(property, icon);
        statsContainer.getChildren().add(viewComponent.getRoot());
    }

    private void updateImage(String image) {
        imageView.setImage(new Image(image));
    }
}
