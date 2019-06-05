package de.uniks.se19.team_g.project_rbsg.login;

import javafx.application.Platform;
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
        setProgress(0.8);
        return loadingIndicatorView;
    }

    public void setProgress(double progress){
        Platform.runLater(() -> progressIndicator.setProgress(progress));
    }

}
