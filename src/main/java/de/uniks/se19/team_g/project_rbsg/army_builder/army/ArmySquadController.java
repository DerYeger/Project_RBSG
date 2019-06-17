package de.uniks.se19.team_g.project_rbsg.army_builder.army;

import de.uniks.se19.team_g.project_rbsg.army_builder.ArmyBuilderState;
import de.uniks.se19.team_g.project_rbsg.util.JavaFXUtils;
import javafx.beans.binding.Bindings;
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
public class ArmySquadController extends ListCell<SquadViewModel> implements Initializable {

    public Label typeLabel;
    public Label countLabel;
    public Pane root;
    public ImageView imageView;

    private SquadViewModel squad;

    private final ArmyBuilderState armyBuilderState;

    public ArmySquadController(ArmyBuilderState armyBuilderState) {
        this.armyBuilderState = armyBuilderState;
    }

    @Override
    protected void updateItem(final SquadViewModel squad, final boolean empty) {
        this.squad = squad;
        super.updateItem(squad, empty);

        if (squad == null || empty) {
            setText(null);
            setGraphic(null);
            return;
        }

        typeLabel.textProperty().bind(squad.members.get(0).name);
        countLabel.textProperty().bind(
            Bindings.size(squad.members).asString()
        );
        JavaFXUtils.bindImage(imageView.imageProperty(), squad.members.get(0).imageUrl);

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
        armyBuilderState.selectedUnit.set(squad.members.get(0));
    }


}
