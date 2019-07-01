package de.uniks.se19.team_g.project_rbsg.army_builder.edit_army;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import org.springframework.stereotype.Component;

@Component
public class EditArmyController {

    public ListView iconList;
    public ImageView selectedIcon;
    public TextField nameInput;
    public Label symbolLabel;
    public Label nameLabel;
    public Label formTitle;

    public void onConfirm() {

    }

    public void onCancel() {

    }
}
