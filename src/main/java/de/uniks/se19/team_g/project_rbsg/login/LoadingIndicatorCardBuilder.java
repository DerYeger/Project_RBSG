package de.uniks.se19.team_g.project_rbsg.login;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;

import java.io.IOException;

public class LoadingIndicatorCardBuilder {

    @FXML
    private Label progressLabel;
    @FXML
    private ProgressIndicator progressIndicator;

    private FXMLLoader fxmlLoader;
    private Node loadingIndicatorView;
    public SimpleDoubleProperty progress;

    public Node buildProgressIndicatorCard(){
        if(fxmlLoader == null) {
            fxmlLoader = new FXMLLoader(getClass().getResource("loading-indicator.fxml"));
            fxmlLoader.setController(this);
            try {
                loadingIndicatorView = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(progress == null){
            progress = new SimpleDoubleProperty(0.0);
        }
        progressIndicator.progressProperty().bind(progress);
        return loadingIndicatorView;
    }

    public void setProgress(double progressDouble) {
        progress.set(progressDouble);
    }

}
