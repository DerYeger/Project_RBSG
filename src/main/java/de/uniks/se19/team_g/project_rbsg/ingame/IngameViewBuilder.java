package de.uniks.se19.team_g.project_rbsg.ingame;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author  Keanu Stückrad
 */
@Component
public class IngameViewBuilder {

    private Node ingameView;

    @Autowired
    private FXMLLoader fxmlLoader;

    public Node buildIngameView() throws Exception{
        if(ingameView == null) {
            fxmlLoader.setLocation(getClass().getResource("ingame-view.fxml"));
            ingameView = fxmlLoader.load();
            final IngameViewController ingameViewController = fxmlLoader.getController();
            ingameViewController.init();
        }
        return ingameView;
    }
}
