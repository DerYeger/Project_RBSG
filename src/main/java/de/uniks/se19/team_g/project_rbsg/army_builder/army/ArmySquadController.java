package de.uniks.se19.team_g.project_rbsg.army_builder.army;

import de.uniks.se19.team_g.project_rbsg.army_builder.ArmyBuilderState;
import de.uniks.se19.team_g.project_rbsg.model.Unit;
import de.uniks.se19.team_g.project_rbsg.util.JavaFXUtils;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@Scope("prototype")
public class ArmySquadController extends ListCell<Unit> implements Initializable {

    public Label typeLabel;
    public Label countLabel;
    public Pane root;
    public ImageView imageView;

    private Unit unit;

    private final ArmyBuilderState armyBuilderState;

    public ArmySquadController(ArmyBuilderState armyBuilderState) {
        this.armyBuilderState = armyBuilderState;
    }

    @Override
    protected void updateItem(final Unit unit, final boolean empty) {
        this.unit = unit;
        super.updateItem(unit, empty);

        if (unit == null || empty) {
            setText(null);
            setGraphic(null);
            return;
        }

        typeLabel.textProperty().bind(unit.name);
        countLabel.setText("1");
        JavaFXUtils.bindImage(imageView.imageProperty(), unit.imageUrl);

        setGraphic(root);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setText(null);
        setGraphic(null);

        root.prefHeightProperty().bindBidirectional(prefWidthProperty());

        root.setOnMouseClicked(this::onSelection);
    }

    private void onSelection(MouseEvent mouseEvent) {
        armyBuilderState.selectedUnit.set(unit);
    }
}
