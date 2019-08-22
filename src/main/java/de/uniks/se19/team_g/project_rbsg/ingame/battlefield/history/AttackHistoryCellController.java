package de.uniks.se19.team_g.project_rbsg.ingame.battlefield.history;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import org.springframework.stereotype.Component;

@Component
public class AttackHistoryCellController {

    public ImageView primaryIcon;
    public ImageView secondaryIcon;
    public Label healthValue;

    public void setHealth(int value) {
        healthValue.setText(String.valueOf(value));
    }
}
