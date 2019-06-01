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
public class StatusBarBuilder {

    @FXML
    private Label statusBarLabel;

    @FXML
    private ImageView statusBarImageView;

    private FXMLLoader fxmlLoader;
    private Node statusBarView;
    private int otherPlayersCount;

    public StatusBarBuilder(int otherPlayersCount){
        this.otherPlayersCount = otherPlayersCount;
    }

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
        setStatusBarPlayerCount(otherPlayersCount);
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
