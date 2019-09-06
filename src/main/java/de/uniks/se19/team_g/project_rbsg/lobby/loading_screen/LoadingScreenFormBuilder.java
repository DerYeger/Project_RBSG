package de.uniks.se19.team_g.project_rbsg.lobby.loading_screen;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoadingScreenFormBuilder {

    private Node loadingScreenForm;
    private FXMLLoader fxmlLoader;
    private LoadingScreenController loadingScreenController;

    @Autowired
    public LoadingScreenFormBuilder(FXMLLoader fxmlLoader){ this.fxmlLoader = fxmlLoader;}

    public Node getLoadingScreenForm() throws IOException {
        if(this.loadingScreenForm == null){
            fxmlLoader.setLocation(LoadingScreenFormBuilder.class.getResource("/ui/lobby/loading_screen/loadingScreen.fxml"));
            loadingScreenForm = fxmlLoader.load();

            loadingScreenController = fxmlLoader.getController();
            loadingScreenController.init();
        }
        return loadingScreenForm;
    }


    public LoadingScreenController getLoadingScreenController() {
        return loadingScreenController;
    }

    public void setLoadingScreenController(LoadingScreenController loadingScreenController) {
        this.loadingScreenController = loadingScreenController;
    }
}
