package de.uniks.se19.team_g.project_rbsg.army_builder.army_selection;

import de.uniks.se19.team_g.project_rbsg.configuration.ApplicationState;
import de.uniks.se19.team_g.project_rbsg.model.Army;
import javafx.beans.binding.Bindings;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.net.URL;
import java.util.ResourceBundle;

@Component
@Scope("prototype")
public class ArmySelectorCellController extends ListCell<Army> implements Initializable {

    public Node root;
    public ImageView imageView;

    @Override
    protected void updateItem(Army item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setGraphic(null);
            return;
        }

        setGraphic(root);
        imageView.imageProperty().bind(
            Bindings.createObjectBinding(() -> item.iconType.get().getImage(), item.iconType)
        );
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setText(null);
        setGraphic(null);
    }
}
