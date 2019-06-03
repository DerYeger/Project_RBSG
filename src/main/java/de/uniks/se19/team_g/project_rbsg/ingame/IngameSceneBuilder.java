package de.uniks.se19.team_g.project_rbsg.ingame;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * @author  Keanu Stückrad
 */
@Component
public class IngameSceneBuilder {

    @Autowired
    private IngameViewBuilder ingameViewBuilder;

    private Scene ingameScene;

    @NonNull
    public Scene getIngameScene() throws Exception {
        if (ingameScene == null) {
            Node ingameNode = ingameViewBuilder.buildIngameView();
            ingameScene = new Scene((Parent) ingameNode);
        }
        return ingameScene;
    }
}
