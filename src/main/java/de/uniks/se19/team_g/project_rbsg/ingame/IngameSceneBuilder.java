package de.uniks.se19.team_g.project_rbsg.ingame;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class IngameSceneBuilder {

    @Autowired
    private IngameViewBuilder ingameViewBuilder;

    private Scene ingameScene;

    @NonNull
    public Scene getIngameScene() throws Exception {
        if (ingameScene == null) {
            Node ingameNode = ingameViewBuilder.buildIngameView();
            final AnchorPane parent = new AnchorPane(ingameNode);
            AnchorPane.setBottomAnchor(ingameNode, 0.0);
            AnchorPane.setLeftAnchor(ingameNode, 0.0);
            AnchorPane.setRightAnchor(ingameNode, 0.0);
            AnchorPane.setTopAnchor(ingameNode, 0.0);
            ingameScene = new Scene(parent);
        }
        return ingameScene;
    }
}
