package de.uniks.se19.team_g.project_rbsg.Ingame;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class StatusBar {

    @FXML
    private Label statusBarLabel;

    @FXML
    private ImageView statusBarImageView;

    private FXMLLoader fxmlLoader;
    private Node statusBarView;

    public Node buildStatusBar(){
        if(fxmlLoader == null) {
            fxmlLoader = new FXMLLoader(getClass().getResource("statusBar.fxml"));
            fxmlLoader.setController(this);
            try {
                statusBarView = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        statusBarLabel.setText("Waiting for 1 more player");
        Image image = new Image(String.valueOf(getClass().getResource("Images/waiting.gif")));
        statusBarImageView.setImage(image);
        return statusBarView;
    }

    public Node setStatusBarPlayerCount(int playerCount){
        if(fxmlLoader == null) {
            buildStatusBar();
        }
        statusBarLabel.setText("Waiting for " + playerCount + " more player");
        return statusBarView;
    }

}
