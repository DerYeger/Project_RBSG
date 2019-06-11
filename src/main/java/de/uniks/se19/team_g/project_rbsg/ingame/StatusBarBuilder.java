package de.uniks.se19.team_g.project_rbsg.ingame;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.util.Duration;

import java.io.IOException;

/**
 * @author  Keanu Stückrad
 */
public class StatusBarBuilder {

    @FXML
    private Label statusBarLabel;

    @FXML
    private ProgressIndicator progressIndicator;

    private FXMLLoader fxmlLoader;
    private Node statusBarView;
    private int otherPlayersCount;
    private SimpleDoubleProperty progress;

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
        if(progress == null){
            progress = new SimpleDoubleProperty(0.0);
        }
        progressIndicator.progressProperty().bind(progress);
        Timeline progressTimeline = setupTimeline();
        progressTimeline.setCycleCount(Animation.INDEFINITE);
        progressTimeline.play();
        setStatusBarPlayerCount(otherPlayersCount);
        return statusBarView;
    }

    public Node setStatusBarPlayerCount(int playerCount){
        if(fxmlLoader == null) {
            buildStatusBar();
        }
        statusBarLabel.setText("Waiting for " + playerCount + " more player...");
        return statusBarView;
    }

    private Timeline setupTimeline() {
        return new Timeline(
                new KeyFrame(Duration.millis(2000),
                        new KeyValue(progress, 1.0)
                )
        );
    }

}
