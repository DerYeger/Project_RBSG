package de.uniks.se19.team_g.project_rbsg.ingame;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;

/**
 * @author  Keanu Stückrad
 */
public class LeaveGameButtonBuilder {

    @FXML
    private Label leaveGameLabel;

    @FXML
    private ImageView leaveGameImage;

    private FXMLLoader fxmlLoader;
    private Node leaveGameButtonView;

    public Node buildLeaveGame(){
        if(fxmlLoader == null) {
            fxmlLoader = new FXMLLoader(getClass().getResource("leaveGame.fxml"));
            fxmlLoader.setController(this);
            try {
                leaveGameButtonView = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        leaveGameLabel.setText("Leave game");
        Image image = new Image(String.valueOf(getClass().getResource("Images/baseline_highlight_off_white_48dp.png")));
        leaveGameImage.setImage(image);
        return leaveGameButtonView;
    }

}
