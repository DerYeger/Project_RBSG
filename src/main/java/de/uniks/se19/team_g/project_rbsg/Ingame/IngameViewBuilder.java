package de.uniks.se19.team_g.project_rbsg.Ingame;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IngameViewBuilder {

    private Node ingameView;

    private FXMLLoader fxmlLoader;

    @Autowired
    public IngameViewBuilder(FXMLLoader fxmlLoader) {
        this.fxmlLoader = fxmlLoader;
    }

    public Node buildIngameScene() throws Exception{
        if(ingameView == null) {
            fxmlLoader.setLocation(getClass().getResource("ingame-view.fxml"));
            ingameView = fxmlLoader.load();
            final IngameViewController ingameViewController = fxmlLoader.getController();
            ingameViewController.init();
        }
        return ingameView;
    }
}
