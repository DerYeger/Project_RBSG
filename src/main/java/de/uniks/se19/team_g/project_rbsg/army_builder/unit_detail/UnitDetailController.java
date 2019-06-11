package de.uniks.se19.team_g.project_rbsg.army_builder.unit_detail;

import de.uniks.se19.team_g.project_rbsg.model.Unit;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;

public class UnitDetailController {
    public StackPane imageStackPane;
    public ImageView imageView;
    public TilePane statsContainer;
    public TextField unitDescription;

    public void bind(Unit unit)
    {
        unitDescription.textProperty().bind(unit.description);
        updateImage(unit.imageUrl.get());
        unit.imageUrl.addListener( (observable, old, next) -> updateImage(next));

    }

    private void updateImage(String image) {
        imageView.setImage(new Image(image));
    }
}
