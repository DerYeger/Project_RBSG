package de.uniks.se19.team_g.project_rbsg.army_builder.edit_army;

import de.uniks.se19.team_g.project_rbsg.configuration.flavor.ArmyIcon;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@Scope("prototype")
public class IconCellController extends ListCell<ArmyIcon> implements Initializable {

    public Node root;
    public ImageView imageView;

    @Override
    protected void updateItem(ArmyIcon item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setGraphic(null);
            return;
        }

        setGraphic(root);
        imageView.setImage(item.getImage());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setText(null);
        setGraphic(null);
    }

    public void onItemClicked()
    {

    }
}
