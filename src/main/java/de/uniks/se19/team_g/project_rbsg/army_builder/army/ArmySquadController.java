package de.uniks.se19.team_g.project_rbsg.army_builder.army;

import de.uniks.se19.team_g.project_rbsg.army_builder.ArmyBuilderState;
import de.uniks.se19.team_g.project_rbsg.configuration.flavor.*;
import de.uniks.se19.team_g.project_rbsg.model.Unit;
import de.uniks.se19.team_g.project_rbsg.util.JavaFXUtils;
import javafx.beans.*;
import javafx.beans.binding.Bindings;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.net.URL;
import java.util.ResourceBundle;

@Component
@Scope("prototype")
public class ArmySquadController extends ListCell<SquadViewModel> implements Initializable {

    public Pane root;
    public ImageView imageView;

    private SquadViewModel squad;

    @Nonnull
    private final ArmyBuilderState armyBuilderState;

    public ArmySquadController(
        @Nonnull ArmyBuilderState armyBuilderState
    ) {
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

        prefHeightProperty().bindBidirectional(prefWidthProperty());

        final Unit deputy = squad.members.get(0);
//        imageView.setImage(deputy.getTypeInfo().getPreview());
        JavaFXUtils.bindImage(imageView.imageProperty(), deputy.imageUrl);

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
